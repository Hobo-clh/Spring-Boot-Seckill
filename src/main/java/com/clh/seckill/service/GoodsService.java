package com.clh.seckill.service;

import com.clh.seckill.mapper.GoodsMapper;
import com.clh.seckill.model.Goods;
import com.clh.seckill.model.GoodsExtend;
import com.clh.seckill.model.SeckillGoods;
import com.clh.seckill.redis.GoodsKey;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: LongHua
 * @date: 2020/7/19
 **/
@Service
public class GoodsService {

    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private RedisService redisService;

    public List<GoodsExtend> listGoodsExtend() {
        return goodsMapper.listGoodsExtend();
    }

    /**
     * 缓存+数据库 通过goodsId查询GoodsExtend
     * @param goodsId
     * @return
     */
    public GoodsExtend getGoodsExtendByGoodsId(long goodsId) {
        //查缓存
        GoodsExtend goodsExtend = redisService.get(GoodsKey.byId, "" + goodsId, GoodsExtend.class);
        if (goodsExtend != null) {
            return goodsExtend;
        }
        //缓存没有查数据库
        GoodsExtend dbGoodsExtend = goodsMapper.getGoodsExtendByGoodsId(goodsId);
        if (dbGoodsExtend != null) {
            //查完数据库放缓存中
            redisService.set(GoodsKey.byId, "" + goodsId, dbGoodsExtend);
        }
        return dbGoodsExtend;
    }

    public void reduceStock(GoodsExtend g) {
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setGoodsId(g.getId());
        seckillGoods.setStockCount(g.getStockCount());
        int i = goodsMapper.reduceStock(seckillGoods);
        System.out.println(i);
    }
}
