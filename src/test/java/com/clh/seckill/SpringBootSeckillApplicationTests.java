package com.clh.seckill;

import com.clh.seckill.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@SpringBootTest(classes = SpringBootSeckillApplication.class)
class SpringBootSeckillApplicationTests {

    @Resource
    RedisService redisService;

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");//代替simpleDateFormat
        LocalDateTime date = LocalDateTime.now();
        System.out.println("DateTimeFormatter:"+dtf.format(date));
        //文件去向

    }
}
class MyTest{
    public static void main(String[] args) {
        HashMap<Object, Boolean> objectObjectHashMap = new HashMap<>();
        Boolean aBoolean = objectObjectHashMap.get("1");
        if (aBoolean) {
            System.out.println(aBoolean);
        }
    }
}