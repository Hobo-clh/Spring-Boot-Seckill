package com.clh.seckill.rabbitmq;

import com.clh.seckill.dto.SeckillMessageDTO;
import com.clh.seckill.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: LongHua
 * @date: 2020/7/29
 **/
@Slf4j
@Service
public class MQSender {

    @Resource
    AmqpTemplate amqpTemplate;

    public void sendSeckillMessage(SeckillMessageDTO messageDTO) {
        String msg = BeanUtil.beanToString(messageDTO);
        log.info("send message: {}", msg);
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, msg);
    }

    // public void send(Object message) {
    //     String msg = BeanUtil.beanToString(message);
    //     log.info("send message: {}", msg);
    //     amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);

    // }
    // public void sendTopic(Object message) {
    //     String msg = BeanUtil.beanToString(message);
    //     log.info("send topic message: {}", msg);
    //     amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, MQConfig.ROUTING_KEY1, msg + "1");
    //     amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, MQConfig.ROUTING_KEY2, msg + "2");
    // }
    //
    // public void sendFanout(Object message) {
    //     String msg = BeanUtil.beanToString(message);
    //     log.info("send fanout message:" + msg);
    //     amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg);
    // }
    //
    // public void sendHeader(Object message) {
    //     String msg = BeanUtil.beanToString(message);
    //     log.info("send fanout message:" + msg);
    //     MessageProperties properties = new MessageProperties();
    //     properties.setHeader("header1", "value1");
    //     properties.setHeader("header2", "value2");
    //     Message obj = new Message(msg.getBytes(), properties);
    //     amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);

    // }
}
