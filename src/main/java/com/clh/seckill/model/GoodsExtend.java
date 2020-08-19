package com.clh.seckill.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: LongHua
 * @date: 2020/7/19
 **/
@Data
public class GoodsExtend extends Goods implements Serializable {
    private Double seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
