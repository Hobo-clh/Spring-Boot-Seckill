package com.clh.seckill.rabbitmq;

import com.clh.seckill.dto.SeckillEmailDTO;
import com.clh.seckill.dto.SeckillMessageDTO;
import com.clh.seckill.model.GoodsExtend;
import com.clh.seckill.model.SeckillOrder;
import com.clh.seckill.model.User;
import com.clh.seckill.service.EmailService;
import com.clh.seckill.service.GoodsService;
import com.clh.seckill.service.OrderService;
import com.clh.seckill.service.SeckillService;
import com.clh.seckill.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: LongHua
 * @date: 2020/7/29
 **/
@Slf4j
@Service
public class MQReceiver {

    @Resource
    private GoodsService goodsService;
    @Resource
    private OrderService orderService;
    @Resource
    private SeckillService seckillService;
    @Resource
    private EmailService emailService;

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receiveSeckillMessage(String message) {
        log.info("receive message: {}", message);
        SeckillMessageDTO messageDTO = BeanUtil.stringToBean(message, SeckillMessageDTO.class);
        //减库存 下订单
        User user = messageDTO.getUser();
        Long goodsId = messageDTO.getGoodsId();

        GoodsExtend goodsExtend = goodsService.getGoodsExtendByGoodsId(goodsId);
        Integer stockCount = goodsExtend.getStockCount();
        if (stockCount <= 0) {
            return;
        }
        SeckillOrder seckillOrder = orderService.getSecOrderByUIdGoodsId(user.getId(), goodsId);
        if (seckillOrder != null) {
            return;
        }
        seckillService.toSeckill(user, goodsExtend);
    }

    @RabbitListener(queues = MQConfig.EMAIL_QUEUE)
    public void receiveEmail(String message) {
        log.info("receive message: {}", message);
        SeckillEmailDTO seckillEmailDTO = BeanUtil.stringToBean(message, SeckillEmailDTO.class);

        //减库存 下订单
        String email = seckillEmailDTO.getEmail();
        Long goodsId = seckillEmailDTO.getGoodsId();

        GoodsExtend goodsExtend = goodsService.getGoodsExtendByGoodsId(goodsId);
        Integer stockCount = goodsExtend.getStockCount();
        if (stockCount <= 0) {
            return;
        }
        emailService.sendSeckillSuccess(email,goodsExtend);
    }




    // @RabbitListener(queues = MQConfig.QUEUE)
    // public void receive(String message) {
    //     log.info("receive message: {}", message);
    // }

    // @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    // public void receiveTopic1(String message) {
    //     log.info("receive topic1 message: {}", message);
    // }
    // @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    // public void receiveTopic2(String message) {
    //     log.info("receive topic2 message: {}", message);
    // }


    // @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    // public void receiveHeaderQueue(byte[] message) {
    //     log.info(" header queue message:" + new String(message));
    // }
}
