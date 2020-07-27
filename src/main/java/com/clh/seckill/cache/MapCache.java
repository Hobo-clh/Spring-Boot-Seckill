package com.clh.seckill.cache;

import com.clh.seckill.dto.OrderDetailDTO;

import java.util.HashMap;

/**
 * @author: LongHua
 * @date: 2020/7/26
 **/
public class MapCache {
    public static HashMap<Long, OrderDetailDTO> cacheMap = new HashMap(16);

}
