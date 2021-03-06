package com.clh.seckill;

import com.clh.seckill.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest(classes = SpringBootSeckillApplication.class)
class SpringBootSeckillApplicationTests {

    @Resource
    RedisService redisService;

    @Test
    void contextLoads() {
        List<String> list = new ArrayList<>();
        list.add("111");
        Class listObj = getListObj(list);
        System.out.println(listObj);
    }


    public static <T> Class getListObj(List<T> list){

        Field[] fields = list.getClass().getDeclaredFields();

        for(Field f : fields){
            f.setAccessible(true);
            if(f.getType() == java.util.List.class){
                // 如果是List类型，得到其Generic的类型
                Type genericType = f.getGenericType();
                if(genericType == null) continue;
                // 如果是泛型参数的类型
                if(genericType instanceof ParameterizedType){
                    ParameterizedType pt = (ParameterizedType) genericType;
                    //得到泛型里的class类型对象
                    Class<?> genericClazz = (Class<?>)pt.getActualTypeArguments()[0];
                    return genericClazz;
                }
            }
        }
        return null;

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