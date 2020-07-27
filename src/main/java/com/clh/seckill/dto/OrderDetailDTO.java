package com.clh.seckill.dto;

import com.clh.seckill.model.GoodsExtend;
import com.clh.seckill.model.OrderInfo;
import com.clh.seckill.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: LongHua
 * @date: 2020/7/25
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {
    User user;
    OrderInfo orderInfo;
    GoodsExtend goodsExtend;
}
