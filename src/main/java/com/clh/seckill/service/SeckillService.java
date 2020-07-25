package com.clh.seckill.service;

import com.clh.seckill.model.GoodsExtend;
import com.clh.seckill.model.OrderInfo;
import com.clh.seckill.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author: LongHua
 * @date: 2020/7/20
 **/
@Service
public class SeckillService {

    @Resource
    private GoodsService goodsService;
    @Resource
    private OrderService orderService;


    @Transactional(rollbackFor = Exception.class)
    public OrderInfo toSeckill(User user, GoodsExtend goods) {
        //减库存
        goodsService.reduceStock(goods);
        //创建订单表、秒杀订单表
        return orderService.createOrder(user,goods);
    }
}
