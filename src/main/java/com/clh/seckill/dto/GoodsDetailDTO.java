package com.clh.seckill.dto;

import com.clh.seckill.model.GoodsExtend;
import com.clh.seckill.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: LongHua
 * @date: 2020/7/24
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetailDTO {
    Integer seckillStatus;
    Integer remainSeconds;
    User user;
    GoodsExtend goodsExtend;
}
