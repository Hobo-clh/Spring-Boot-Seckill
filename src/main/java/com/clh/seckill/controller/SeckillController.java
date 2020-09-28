package com.clh.seckill.controller;

import com.clh.seckill.access.AccessLimit;
import com.clh.seckill.dto.ResultDTO;
import com.clh.seckill.dto.SeckillEmailDTO;
import com.clh.seckill.dto.SeckillMessageDTO;
import com.clh.seckill.exception.CodeMsgEnum;
import com.clh.seckill.model.GoodsExtend;
import com.clh.seckill.model.OrderInfo;
import com.clh.seckill.model.SeckillOrder;
import com.clh.seckill.model.User;
import com.clh.seckill.rabbitmq.MQSender;
import com.clh.seckill.redis.GoodsKey;
import com.clh.seckill.service.GoodsService;
import com.clh.seckill.service.OrderService;
import com.clh.seckill.service.RedisService;
import com.clh.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: LongHua
 * @date: 2020/7/20
 **/
@Slf4j
@Controller
@RequestMapping("/seckill")
public class SeckillController{
    AtomicBoolean atomicBoolean;
    @Resource
    private GoodsService goodsService;
    @Resource
    private OrderService orderService;
    @Resource
    private SeckillService seckillService;
    @Resource
    private RedisService redisService;
    @Resource
    private MQSender sender;

    public static Map<Long, Boolean> localOverMap = new ConcurrentHashMap<>(16);

    /**
     * 秒杀
     */
    @PostMapping("/do_seckill")
    public String doSeckill(User user, Model model,
                            @RequestParam("goodsId") Long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return "login";
        }
        GoodsExtend goods = goodsService.getGoodsExtendByGoodsId(goodsId);
        if (goods.getStockCount() <= 0) {
            //库存不足
            model.addAttribute("errorMsg", CodeMsgEnum.SECKILL_STOCK_EMPTY.getMsg());
            return "seckill_fail";
        }
        SeckillOrder order = orderService.getSecOrderByUIdGoodsId(user.getId(), goods.getId());
        if (order != null) {
            //说明已经秒杀过了
            model.addAttribute("errorMsg", CodeMsgEnum.SECKILL_REPEAT.getMsg());
            return "seckill_fail";
        }
        //减库存 下订单 写入秒杀订单
        //解决超卖（在数据库简历唯一索引 1、插入时 2、stock_count > 0）
        OrderInfo orderInfo;

        orderInfo = seckillService.toSeckill(user, goods);
        if (orderInfo == null) {
            model.addAttribute("errorMsg", CodeMsgEnum.SECKILL_STOCK_EMPTY.getMsg());
            return "seckill_fail";
        }
        atomicBoolean.get();
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }

    /**
     * 优化思路：减少对数据库的
     * 1、系统初始化，把商品的库存数量加载到Redis
     * 2、收到请求，Redis预减库存，库存不足，直接返回，否则进入3
     * 3、请求入队，立即返回排队中
     * 4、请求出队，生成订单，减少库存
     * 5、客户端轮询，是否秒杀成功
     * QPS:    次数
     * 1391   5000 * 10
     * @return
     */
    @PostMapping("/{path}/toSeckill")
    @ResponseBody
    public ResultDTO doSeckillByUp(User user,
                                   @RequestParam("goodsId") Long goodsId,
                                   @PathVariable(value = "path") String path) {
        //参数校验
        if (user == null) {
            return ResultDTO.error(CodeMsgEnum.USER_IS_EMPTY);
        }
        // 判断path是否合法
        boolean isLegal = seckillService.checkSeckillPath(goodsId, user.getId(), path);
        if (!isLegal) {
            //不合法
            return ResultDTO.error(CodeMsgEnum.REQUEST_ILLEGAL);
        }
        //内存标记
        Boolean isOver = localOverMap.get(goodsId);
        if (isOver) {
            return ResultDTO.error(CodeMsgEnum.SECKILL_STOCK_EMPTY);
        }
        //判断是否重复订单
        SeckillOrder secOrder = orderService.getSecOrderByUIdGoodsId(user.getId(), goodsId);
        if (secOrder != null) {
            return ResultDTO.error(CodeMsgEnum.SECKILL_REPEAT);
        }
        //减库存 返回减少后的库存数量
        Long stockCount = redisService.decr(GoodsKey.getSeckillGoodsStock, goodsId + "");
        if (stockCount < 0) {
            localOverMap.put(goodsId, true);
            return ResultDTO.error(CodeMsgEnum.SECKILL_STOCK_EMPTY);
        }
        //入队
        SeckillMessageDTO messageDTO = new SeckillMessageDTO(user, goodsId);
        sender.sendSeckillMessage(messageDTO);
        if (user.getEmail() != null) {
            sender.sendEmail(new SeckillEmailDTO(user.getEmail(), goodsId));
        }
        //排队中
        return ResultDTO.success(0);
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0：排队中
     */
    @AccessLimit(seconds = 600,maxCount = 100)
    @ResponseBody
    @GetMapping("/result")
    public ResultDTO seckillResult(User user, @RequestParam("goodsId") Long goodsId) {
        Long result = seckillService.getSeckillResult(goodsId, user.getId());
        return ResultDTO.success(result);
    }

    @AccessLimit(seconds = 5, maxCount = 5)
    @ResponseBody
    @PostMapping("/path")
    public ResultDTO seckillPath(
            @RequestParam("goodsId") Long goodsId, User user,
            @RequestParam(value = "verifyCode") String verifyCode) {
        if (StringUtils.isBlank(verifyCode)) {
            return ResultDTO.error(CodeMsgEnum.VERIFY_CODE_IS_EMPTY);
        }
        //判断验证码是否正确
        boolean isCorrect = seckillService.checkVerifyCode(goodsId, user.getId(), verifyCode);
        if (!isCorrect) {
            return ResultDTO.error(CodeMsgEnum.VERIFY_CODE_IS_ERROR);
        }
        String path = seckillService.createSeckillPath(goodsId, user.getId());
        return ResultDTO.success(path);
    }

    @GetMapping("/verifyCode")
    @ResponseBody
    public ResultDTO getSeckillVerifyCode(@RequestParam("goodsId") Long goodsId,
                                          HttpServletResponse response,
                                          User user) {
        if (user == null) {
            return ResultDTO.error(CodeMsgEnum.SESSION_ERROR);
        }
        BufferedImage image = seckillService.createVerifyCodeImg(goodsId, user.getId());
        try (ServletOutputStream out = response.getOutputStream()) {
            ImageIO.write(image, "JPEG", out);
            out.flush();
            return ResultDTO.success(null);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResultDTO.error(CodeMsgEnum.SECKILL_FAIL);
        }
    }
}
