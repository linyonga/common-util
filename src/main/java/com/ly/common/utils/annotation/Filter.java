package com.ly.common.utils.annotation;

import com.google.common.base.Joiner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;
import java.util.Map;


/**
 * 过滤参数 非必须 可以有多个
 *
 * @author: yong.lin
 * @date: 2016-10-27  20:01
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Filter {
    String VERTICAL = "|";

    //过滤的检索字段名称 不填则为加了该注解的字段名称
    String field() default "";


    FilterType filerType();

    //范围查询时需要指定是开始还是结束
    RangeType rangeType() default RangeType.start;

    enum FilterType {
        eq("+%s:%s") {
            @Override
            public String apply(String field, Object value) {
                return String.format(formatter, field, value);
            }
        },
        nq("-%s:%s") {
            @Override
            public String apply(String field, Object value) {
                return String.format(formatter, field, value);
            }
        },
        in("%s IN (%s)") {
            @Override
            public String apply(String field, Object value) {
                Joiner joiner = Joiner.on(VERTICAL).skipNulls();
                return String.format(formatter, field,
                        joiner.join((Iterable<?>) value));
            }
        },
        range("%s:[%s TO %s]") {
            @Override
            public String apply(String field, Object value) {
                @SuppressWarnings("unchecked")
                Map<RangeType, Object> map = (Map<RangeType, Object>) value;
                return String.format(formatter, field, getRange(map.get(RangeType.start)),
                        getRange(map.get(RangeType.end)));
            }

            private Object getRange(Object object) {
                if (object != null && object instanceof Date) {
                    object = ((Date) object).getTime();
                }
                return nullToAsterisk(object);
            }
        };

        FilterType(String formatter) {
            this.formatter = formatter;
        }

        Object nullToAsterisk(Object value) {
            return value == null ? "*" : value;
        }

        protected String formatter;

        public String getFormatter() {
            return formatter;
        }


        public abstract String apply(String field, Object value);
    }

    enum RangeType {
        start,
        end
    }

}
