package com.clh.seckill.model;

import lombok.Data;

import java.util.Date;

/**
 * @author: LongHua
 * @date: 2020/7/19
 **/
@Data
public class SeckillGoods {
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
