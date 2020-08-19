package com.clh.seckill.controller;

import com.clh.seckill.access.AccessLimit;
import com.clh.seckill.dto.GoodsDetailDTO;
import com.clh.seckill.dto.ResultDTO;
import com.clh.seckill.model.GoodsExtend;
import com.clh.seckill.model.User;
import com.clh.seckill.redis.GoodsKey;
import com.clh.seckill.service.GoodsService;
import com.clh.seckill.service.RedisService;
import com.clh.seckill.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: LongHua
 * @date: 2020/7/18
 **/
@Slf4j
@Controller
public class GoodsController {

    @Resource
    private RedisService redisService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private ThymeleafViewResolver resolver;

    // /**
    //  * 要把页面缓存换成物品缓存
    //  *
    //  * @param model
    //  * @param user
    //  * @return
    //  */
    // @AccessLimit(seconds = 60, maxCount = 2000, needLogin = false)
    // @GetMapping(value = "/goods_list", produces = "text/html")
    // @ResponseBody
    // public String goodsPage(HttpServletRequest request,
    //                         HttpServletResponse response,
    //                         Model model, User user) {
    //     //从redis缓存中查询是否有页面缓存
    //     //取缓存
    //     String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
    //     if (StringUtils.isNotEmpty(html)) {
    //         return html;
    //     }
    //     List<GoodsExtend> goodsExtendList = goodsService.listGoodsExtend();
    //     model.addAttribute("goodsList", goodsExtendList);
    //     model.addAttribute("user", user);
    //
    //     //手动渲染
    //     IWebContext ctx = new WebContext(request, response,
    //             request.getServletContext(), request.getLocale(), model.asMap());
    //     html = resolver.getTemplateEngine().process("goods_list", ctx);
    //     //放入缓存中
    //     if (StringUtils.isNotEmpty(html)) {
    //         redisService.set(GoodsKey.getGoodsList, "", html);
    //     }
    //     return html;
    // }


    @AccessLimit(seconds = 60, maxCount = 2000, needLogin = false)
    @GetMapping(value = "/goods_list")
    public String goodsPage2(HttpServletRequest request,
                             Model model) {
        //取缓存
        String list = redisService.get(GoodsKey.getGoodsList, "", String.class);
        List<GoodsExtend> goodsExtends = BeanUtil.stringToList(list, GoodsExtend.class);
        if (goodsExtends == null || goodsExtends.size() <= 0) {
            goodsExtends = goodsService.listGoodsExtend();
            //放入缓存中
            if (goodsExtends.size() > 0) {
                redisService.set(GoodsKey.getGoodsList, "", goodsExtends);
            }
        }
        model.addAttribute("goodsList", goodsExtends);
        return "goods_list";
    }

    /**
     * 优化后的商品详情业
     *
     * @param user
     * @param goodsId
     * @return
     */
    @AccessLimit(seconds = 20, maxCount = 10, needLogin = false)
    @GetMapping(value = "/goods/detail/{goodsId}")
    @ResponseBody
    public ResultDTO goodsDetail(User user, @PathVariable("goodsId") long goodsId) {
        GoodsExtend goodsExtend = goodsService.getGoodsExtendByGoodsId(goodsId);
        long startAt = goodsExtend.getStartDate().getTime();
        long endAt = goodsExtend.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;
        //seckillStatus ---0代表还没开始 1代表
        if (now < startAt) {
            //秒杀还没开始，倒计时
            seckillStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {
            //秒杀已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailDTO detailDTO = GoodsDetailDTO.builder()
                .user(user)
                .goodsExtend(goodsExtend)
                .remainSeconds(remainSeconds)
                .seckillStatus(seckillStatus)
                .build();

        log.info(detailDTO.toString());
        return ResultDTO.success(detailDTO);
    }
}
