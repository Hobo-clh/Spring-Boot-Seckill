package com.clh.seckill.model;

import lombok.Data;

import java.util.Date;

/**
 * @author: LongHua
 * @date: 2020/7/19
 **/
@Data
public class OrderInfo {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Long  deliveryAddrId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;
    private Date createDate;
    private Date payDate;
}
