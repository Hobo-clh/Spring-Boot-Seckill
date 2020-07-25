package com.clh.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.clh.seckill.mapper")
@SpringBootApplication
public class SpringBootSeckillApplication  {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSeckillApplication.class, args);
    }

}


// public class SpringBootSeckillApplication extends SpringBootServletInitializer {
//
//     public static void main(String[] args) {
//         SpringApplication.run(SpringBootSeckillApplication.class, args);
//     }
//
//     @Override
//     protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//         return builder.sources(SpringBootSeckillApplication.class);
//     }
// }
