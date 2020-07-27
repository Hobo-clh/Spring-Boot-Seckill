package com.clh.seckill.controller;

import com.clh.seckill.cache.MapCache;
import com.clh.seckill.dto.OrderDetailDTO;
import com.clh.seckill.dto.ResultDTO;
import com.clh.seckill.exception.CodeMsgEnum;
import com.clh.seckill.exception.GlobleException;
import com.clh.seckill.model.GoodsExtend;
import com.clh.seckill.model.OrderInfo;
import com.clh.seckill.model.SeckillOrder;
import com.clh.seckill.model.User;
import com.clh.seckill.service.GoodsService;
import com.clh.seckill.service.OrderService;
import com.clh.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: LongHua
 * @date: 2020/7/20
 **/
@Slf4j
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Resource
    private GoodsService goodsService;
    @Resource
    private OrderService orderService;
    @Resource
    private SeckillService seckillService;


    /**
     * 2G内存的CentOS虚拟机 QPS：517
     *      500 * 10s
     *
     * 本机 QPS：905
     *      500 * 10s
     * 修改成异步
     * @param user
     * @param model
     * @param goodsId
     * @return
     */
    @PostMapping("/do_seckill")
    public String list(User user, Model model,
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
        OrderInfo orderInfo = seckillService.toSeckill(user, goods);

        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }


    @PostMapping("/asynSeckill")
    @ResponseBody
    public ResultDTO list2(User user,
                           @RequestParam("goodsId") Long goodsId) {
        if (user == null) {
            return ResultDTO.error(CodeMsgEnum.USER_IS_EMPTY);
        }
        GoodsExtend goods = goodsService.getGoodsExtendByGoodsId(goodsId);
        if (goods.getStockCount() <= 0) {
            //库存不足
            ResultDTO.error(CodeMsgEnum.SECKILL_STOCK_EMPTY);
        }
        SeckillOrder order = orderService.getSecOrderByUIdGoodsId(user.getId(), goods.getId());
        if (order != null) {
            //说明已经秒杀过了
            return ResultDTO.error(CodeMsgEnum.SECKILL_REPEAT);
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo;
        try {
            orderInfo = seckillService.toSeckill(user, goods);
        }catch (GlobleException e){
            return ResultDTO.error(CodeMsgEnum.SECKILL_STOCK_EMPTY);
        }
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO(user,orderInfo,goods);
        MapCache.cacheMap.put(orderInfo.getId(), orderDetailDTO);
        return ResultDTO.success(orderDetailDTO);
    }


}
