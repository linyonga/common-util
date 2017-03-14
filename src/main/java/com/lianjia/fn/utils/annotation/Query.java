package com.lianjia.fn.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 检索关键词，放在多个字段时，多个字段都有值时，只取一个
 *
 * @author: yong.lin
 * @date: 2016-10-27  19:52
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    //检索字段 多个字段使用英文逗号（,）分隔 默认为当前字段名称
    String qf() default "";
}
