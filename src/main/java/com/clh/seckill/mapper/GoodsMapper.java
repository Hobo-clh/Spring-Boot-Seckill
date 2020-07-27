package com.clh.seckill.mapper;

import com.clh.seckill.model.GoodsExtend;
import com.clh.seckill.model.SeckillGoods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author: LongHua
 * @date: 2020/7/19
 **/
public interface GoodsMapper {

    /**
     * 查询扩展商品列表
     *
     * @return 扩展商品列表
     */
    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.seckill_price from goods_seckill mg " +
            "left join goods g on mg.goods_id = g.id")
    List<GoodsExtend> listGoodsExtend();

    /**
     * 通过商品id查询扩展商品
     *
     * @param goodsId 商品id
     * @return 扩展商品
     */
    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.seckill_price from goods_seckill mg " +
            "left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
    GoodsExtend getGoodsExtendByGoodsId(@Param("goodsId") long goodsId);

    /**
     * 更新秒杀商品库存
     *
     * @param g
     * @return
     */
    @Update("update goods_seckill set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    int reduceStock(SeckillGoods g);
}
