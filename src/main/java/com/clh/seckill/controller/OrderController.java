package com.clh.seckill.controller;

import com.clh.seckill.access.AccessLimit;
import com.clh.seckill.cache.MapCache;
import com.clh.seckill.dto.OrderDetailDTO;
import com.clh.seckill.dto.ResultDTO;
import com.clh.seckill.exception.CodeMsgEnum;
import com.clh.seckill.model.GoodsExtend;
import com.clh.seckill.model.OrderInfo;
import com.clh.seckill.model.User;
import com.clh.seckill.service.GoodsService;
import com.clh.seckill.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author: LongHua
 * @date: 2020/7/26
 **/
@Controller
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private GoodsService goodsService;

    @AccessLimit(seconds = 10,maxCount = 5)
    @GetMapping("/detail/{orderId}")
    @ResponseBody
    public ResultDTO getSeckillOrderById(@PathVariable("orderId") Long id,
                                         User user){
        //先从缓存中查找
        OrderDetailDTO orderDetailDTO = MapCache.cacheMap.get(id);
        //查找到直接返回
        if (orderDetailDTO != null) {
            return ResultDTO.success(orderDetailDTO);
        }
        //没有则去数据库找
        OrderInfo orderInfo = orderService.getOrderById(id);
        //如果数据库也没有返回错误提示
        if (orderInfo == null) {
            return ResultDTO.error(CodeMsgEnum.ORDER_IS_EMPTY);
        }
        GoodsExtend goodsExtend = goodsService.getGoodsExtendByGoodsId(orderInfo.getGoodsId());
        OrderDetailDTO detailDTO = OrderDetailDTO.builder()
                .goodsExtend(goodsExtend)
                .orderInfo(orderInfo)
                .user(user)
                .build();
        MapCache.cacheMap.put(orderInfo.getId(),detailDTO);
        return ResultDTO.success(detailDTO);
    }
}
