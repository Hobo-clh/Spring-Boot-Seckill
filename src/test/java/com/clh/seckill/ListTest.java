package com.clh.seckill;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: LongHua
 * @date: 2020/8/4
 **/
public class ListTest {
    @Test
    public void test() {
        List<String> list = new ArrayList<>();

        System.out.println(list);
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
}
