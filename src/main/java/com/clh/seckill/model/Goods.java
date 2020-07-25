package com.clh.seckill.model;

import lombok.Data;

/**
 * @author: LongHua
 * @date: 2020/7/19
 **/
@Data
public class Goods {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
}
