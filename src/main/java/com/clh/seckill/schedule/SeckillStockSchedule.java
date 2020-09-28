package com.clh.seckill.schedule;

import com.clh.seckill.controller.SeckillController;
import com.clh.seckill.model.GoodsExtend;
import com.clh.seckill.redis.GoodsKey;
import com.clh.seckill.service.GoodsService;
import com.clh.seckill.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: LongHua
 * @date: 2020/8/22
 **/
@EnableScheduling
@Component
@Slf4j
public class SeckillStockSchedule {

    @Resource
    private RedisService redisService;
    @Resource
    private GoodsService goodsService;

    /**
     * 系统初始化时将库存放入至缓存中
     */
    public void afterPropertiesSet() {
        List<GoodsExtend> goodsExtends = goodsService.listGoodsExtend();
        if (goodsExtends == null || goodsExtends.size() == 0) {
            return;
        }
        for (GoodsExtend goodsExtend : goodsExtends) {
            redisService.set(GoodsKey.getSeckillGoodsStock, goodsExtend.getId() + "", goodsExtend.getStockCount());
            if (goodsExtend.getStockCount() > 0) {
                SeckillController.localOverMap.put(goodsExtend.getId(), false);
            } else {
                SeckillController.localOverMap.put(goodsExtend.getId(), true);
            }
        }
    }
    /**
     * 6小时定时
     */
    @Scheduled(fixedRate = 6 * 60 * 60 * 1000)
    public void seckillStockSchedule(){
        log.info("{}:定时任务启动", LocalDateTime.now());
        afterPropertiesSet();
    }
}
