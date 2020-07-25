package com.clh.seckill.controller;

import com.clh.seckill.dto.GoodsDetailDTO;
import com.clh.seckill.dto.ResultDTO;
import com.clh.seckill.model.GoodsExtend;
import com.clh.seckill.model.User;
import com.clh.seckill.redis.GoodsKey;
import com.clh.seckill.service.GoodsService;
import com.clh.seckill.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * <p>
     * 本机  QPS 1215     升级页面缓存后 本机 QPS：2645
     * 500 * 10s
     * <p>
     * QPS 2669
     * 1000 * 10s
     * <p>
     * QPS 1664
     * 5000 * 10s
     *
     * @param model
     * @param user
     * @return
     */
    @GetMapping(value = "/goods_list", produces = "text/html")
    @ResponseBody
    public String goodsPage(HttpServletRequest request,
                            HttpServletResponse response,
                            Model model, User user) {
        //从redis缓存中查询是否有页面缓存
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (StringUtils.isNotEmpty(html)) {
            return html;
        }
        List<GoodsExtend> goodsExtendList = goodsService.listGoodsExtend();
        model.addAttribute("goodsList", goodsExtendList);
        model.addAttribute("user", user);
        //手动渲染
        IWebContext ctx = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = resolver.getTemplateEngine().process("goods_list", ctx);
        //放入缓存中
        if (StringUtils.isNotEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    @GetMapping(value = "/to_detail/{goodsId}", produces = "text/html")
    @ResponseBody
    public String detail(HttpServletRequest request,
                         HttpServletResponse response,
                         Model model, User user,
                         @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);

        GoodsExtend goodsExtend = goodsService.getGoodsExtendByGoodsId(goodsId);
        model.addAttribute("goods", goodsExtend);

        long startAt = goodsExtend.getStartDate().getTime();
        long endAt = goodsExtend.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) {
            //秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);

        } else if (now > endAt) {
            //秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("seckillStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        String html = redisService.get(GoodsKey.getGoodsDetail, String.valueOf(goodsId), String.class);
        if (StringUtils.isNotEmpty(html)) {
            return html;
        }
        WebContext context = new WebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap());
        html = resolver.getTemplateEngine().process("goods_detail", context);
        redisService.set(GoodsKey.getGoodsDetail, String.valueOf(goodsId), html);
        return html;
    }

    @GetMapping(value = "/goods/detail/{goodsId}")
    @ResponseBody
    public ResultDTO detail2(User user, @PathVariable("goodsId") long goodsId) {
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
