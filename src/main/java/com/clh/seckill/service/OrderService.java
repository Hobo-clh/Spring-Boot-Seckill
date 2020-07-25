package com.clh.seckill.service;

import com.clh.seckill.mapper.OrderMapper;
import com.clh.seckill.model.GoodsExtend;
import com.clh.seckill.model.OrderInfo;
import com.clh.seckill.model.SeckillOrder;
import com.clh.seckill.model.User;
import com.clh.seckill.redis.SecKillOrderKey;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: LongHua
 * @date: 2020/7/20
 **/
@Service
public class OrderService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private RedisService redisService;

    /**
     * 缓存+数据库 通过userId和goodsId查询秒杀订单
     * @param userId
     * @param goodsId
     * @return
     */
    public SeckillOrder getSecOrderByUIdGoodsId(Long userId, Long goodsId) {
        SeckillOrder seckillOrder = redisService.get(SecKillOrderKey.getUIdGoodsId, "" + userId + goodsId, SeckillOrder.class);
        if (seckillOrder != null) {
            return seckillOrder;
        }
        SeckillOrder dbSeckillOrder = orderMapper.getSecOrderByUIdGoodsId(userId, goodsId);
        redisService.set(SecKillOrderKey.getUIdGoodsId, "" + userId + goodsId, dbSeckillOrder);
        return dbSeckillOrder;
    }

    public OrderInfo createOrder(User user, GoodsExtend goods) {
        OrderInfo orderInfo = new OrderInfo();

        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(100L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        Long orderId = orderMapper.insert(orderInfo);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderId);
        seckillOrder.setUserId(user.getId());
        orderMapper.insertSeckillOrder(seckillOrder);
        return orderInfo;
    }
}
