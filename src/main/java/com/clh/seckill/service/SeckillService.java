package com.clh.seckill.service;

import com.clh.seckill.model.GoodsExtend;
import com.clh.seckill.model.OrderInfo;
import com.clh.seckill.model.SeckillOrder;
import com.clh.seckill.model.User;
import com.clh.seckill.redis.GoodsKey;
import com.clh.seckill.redis.SeckillKey;
import com.clh.seckill.util.MD5Util;
import com.clh.seckill.util.UUIDUtil;
import com.clh.seckill.util.VerifyCodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;

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
    @Resource
    private RedisService redisService;
    @Resource
    private VerifyCodeUtil verifyCodeUtil;

    /**
     * 秒杀商品
     *
     * @param user  用户
     * @param goods 秒杀商品
     * @return OrderInfo 秒杀成功后返回订单表信息
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderInfo toSeckill(User user, GoodsExtend goods) {
        //减库存 失败
        if (!goodsService.reduceStock(goods)) {
            setGoodsOver(goods.getId());
            return null;
        }
        //更新库存后 删除缓存
        redisService.delete(GoodsKey.byId, "" + goods.getId());
        //创建订单表、秒杀订单表
        return orderService.createOrder(user, goods);
    }

    /**
     * 获取秒杀结果
     *
     * @param goodsId 秒杀商品id
     * @param userId  用户id
     * @return 秒杀成功返回orderId，如果商品还未卖完，返回0，卖完则返回-1
     */
    public Long getSeckillResult(Long goodsId, Long userId) {
        SeckillOrder seckillOrder = orderService.getSecOrderByUIdGoodsId(userId, goodsId);
        if (seckillOrder != null) {
            return seckillOrder.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            return isOver ? -1L : 0L;
        }
    }

    /**
     * 创造秒杀path，并返回
     *
     * @param goodsId 秒杀商品id
     * @param userId  用户id
     * @return 秒杀路径
     */
    public String createSeckillPath(Long goodsId, Long userId) {
        String path = MD5Util.md5(UUIDUtil.uuid() + "qwe");
        redisService.set(SeckillKey.getSeckillPath, goodsId + "_" + userId, path);
        return path;
    }

    /**
     * 检查秒杀path是否存在
     *
     * @param goodsId 秒杀商品id
     * @param userId  用户id
     * @param path    秒杀路径
     * @return 如果path与redis缓存中的pathCache相同，返回true，反之false
     */
    public boolean checkSeckillPath(Long goodsId, Long userId, String path) {
        if (userId == null || goodsId == null || path == null) {
            return false;
        }
        String pathCache = redisService.get(SeckillKey.getSeckillPath, goodsId + "_" + userId, String.class);
        return path.equals(pathCache);
    }

    /**
     * 判断验证码是否正确
     *
     * @param goodsId        秒杀商品id
     * @param userId         用户id
     * @param verifyCodeForm 表单验证码
     * @return 正确返回true, 反正false
     */
    public boolean checkVerifyCode(Long goodsId, Long userId, String verifyCodeForm) {
        if (goodsId == null || userId == null || StringUtils.isEmpty(verifyCodeForm)) {
            return false;
        }
        String verifyCodeCache = redisService.get(SeckillKey.getVerifyCode, goodsId + "_" + userId, String.class);
        //删除验证码
        redisService.delete(SeckillKey.getVerifyCode, goodsId + "_" + userId);
        return verifyCodeForm.equals(verifyCodeCache);
    }

    /**
     * 获取验证码图片
     */
    public BufferedImage createVerifyCodeImg(Long goodsId, Long id) {
        return verifyCodeUtil.createVerifyCode(goodsId, id);
    }

    /**
     * 设置商品已经卖完
     *
     * @param goodsId 秒杀商品id
     */
    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver, goodsId + "", true);
    }

    /**
     * 判断商品是否卖完
     *
     * @param goodsId 秒杀商品id
     * @return 在redis中存在该key，返回true,反正false
     */
    private boolean getGoodsOver(Long goodsId) {
        boolean exists = redisService.exists(SeckillKey.isGoodsOver, goodsId + "");
        return exists;
    }
}
