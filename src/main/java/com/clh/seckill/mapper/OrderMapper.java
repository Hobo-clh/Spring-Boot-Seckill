package com.clh.seckill.mapper;

import com.clh.seckill.model.OrderInfo;
import com.clh.seckill.model.SeckillOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

/**
 * @author: LongHua
 * @date: 2020/7/19
 **/
public interface OrderMapper {
    @Select("select * from order_seckill where user_id=#{userId} and goods_id=#{goodsId}")
    SeckillOrder getSecOrderByUIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=Long.class, before=false, statement="select last_insert_id()")
    Long insert(OrderInfo orderInfo);

    @Insert("insert into order_seckill (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    int insertSeckillOrder(SeckillOrder seckillOrder);
}
