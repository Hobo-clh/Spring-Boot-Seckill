package com.clh.seckill.redis;

/**
 * @author: LongHua
 * @date: 2020/7/23
 **/
public class GoodsKey extends BasePrefix {

    /**
     * 6小时
     */
    private static final int EXPIRE = 60 * 60 * 6;

    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    /**
     * 设置60s缓存时间
     */
    public static GoodsKey getGoodsList = new GoodsKey(EXPIRE, "gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(EXPIRE, "gd");
    public static GoodsKey byId = new GoodsKey(EXPIRE, "id");
    public static GoodsKey getSeckillGoodsStock = new GoodsKey(EXPIRE, "seckillGoodsStock");
}
