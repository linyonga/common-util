package com.ly.common.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可以通过此参数获取本次查询需要的字段内容
 *
 * @author: yong.lin
 * @date: 2016-10-27  19:58
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Fl {

}
