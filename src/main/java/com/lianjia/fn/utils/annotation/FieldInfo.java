package com.lianjia.fn.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: yong.lin
 * @date: 2016-10-19  20:00
 */
@Target({ElementType.FIELD, ElementType.TYPE})

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldInfo {
    String value();

    // 从上下文哪个字段获取值 ,为空时使用当前字段名
    String from() default "";

    // 从上下文中多个字段取值
    String expression() default "";
}
