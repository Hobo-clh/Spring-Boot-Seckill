package com.clh.seckill.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注了此注解的方法
 *
 * <p>
 * maxCount：最大访问次数
 * </p>
 * <p>
 * needLogin：
 * </p>
 *
 * @author: LongHua
 * @date: 2020/7/30
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {
    /**
     * 时间范围内
     */
    int seconds();

    /**
     * 最大访问次数
     */
    int maxCount();

    /**
     * 是否需要登录，默认为true
     */
    boolean needLogin() default true;
}
