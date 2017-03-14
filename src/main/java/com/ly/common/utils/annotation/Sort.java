package com.ly.common.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 排序参数
 *
 * @author: yong.lin
 * @date: 2016-10-27  20:02
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sort {

    String field() default "";

    enum SortType {
        asc("+%s"),
        desc("-%s");

        SortType(String formatter) {
            this.formatter = formatter;
        }

        protected String formatter;

        public static String apply(String sort, String field) {
            for (SortType sortType : values()) {
                if (sortType.name().equals(sort)) {
                    return String.format(sortType.formatter, field);
                }
            }
            throw new IllegalArgumentException(sort + "不合法!");

        }
    }
}
