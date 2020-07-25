package com.clh.seckill.model;

import lombok.Data;

/**
 * @author: LongHua
 * @date: 2020/7/19
 **/
@Data
public class SeckillOrder {
    private Long id;
    private Long userId;
    private Long  orderId;
    private Long goodsId;
}
