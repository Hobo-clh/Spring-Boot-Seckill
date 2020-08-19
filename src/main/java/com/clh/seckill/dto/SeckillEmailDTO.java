package com.clh.seckill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: LongHua
 * @date: 2020/8/19
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillEmailDTO {
    private String email;
    private Long goodsId;
}
