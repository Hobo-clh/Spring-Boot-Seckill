package com.clh.seckill.service;

import com.clh.seckill.exception.CodeMsgEnum;
import com.clh.seckill.exception.GlobleException;
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
        int i = goodsService.reduceStock(goods);
        if (i <= 0) {
            throw new GlobleException(CodeMsgEnum.SECKILL_STOCK_EMPTY);
        }
        return orderService.createOrder(user,goods);
        //创建订单表、秒杀订单表
    }
}
