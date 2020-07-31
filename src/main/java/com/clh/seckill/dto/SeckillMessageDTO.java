package com.clh.seckill.dto;

import com.clh.seckill.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: LongHua
 * @date: 2020/7/30
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMessageDTO {
    private User user;
    private Long goodsId;
}
