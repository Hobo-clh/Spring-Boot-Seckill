package com.clh.seckill.redis;

/**
 * @author: LongHua
 * @date: 2020/7/23
 **/
public class GoodsKey extends BasePrefix{

    private static final int EXPIRE = 60;

    private GoodsKey(int expireSeconds,String prefix) {
        super(prefix);
    }
    /** 设置60s缓存时间 */
    public static GoodsKey getGoodsList = new GoodsKey(EXPIRE,"gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(EXPIRE,"gd");
    public static GoodsKey byId = new GoodsKey(EXPIRE * 60,"id");
}
