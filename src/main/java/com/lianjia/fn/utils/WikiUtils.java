package com.lianjia.fn.utils;


import com.lianjia.fn.utils.annotation.FieldInfo;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


/**
 * @author: yong.lin
 * @date: 2016-10-19  19:26
 */
public class WikiUtils {
    public static final String CLASS = "class";
    public static final String SEPARATOR = ".";

    public static void wikiRet(Class<?> clazz) {
        System.out.println("字段" + "\t" + "字段名称" + "\t" + "类型" + "\t" + "备注");
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field[] fields = clazz.getDeclaredFields();
                print(fields, "");
            } catch (Exception e) {
            }
        }

    }

    public static void wikiParam(Class<?> clazz) {
        System.out.println("字段" + "\t" + "字段名称" + "\t" + "类型" + "\t" + "必填" + "\t" + "备注");
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field[] fields = clazz.getDeclaredFields();
                print(fields, "\t");
            } catch (Exception e) {
            }
        }

    }

    private static void print(Field[] fields, String tab) {
        for (Field f : fields) {
            FieldInfo fieldName = f.getAnnotation(FieldInfo.class);
            NotNull notNull = f.getAnnotation(NotNull.class);
            if (fieldName != null) {
                String type = getFieldType(f);

                System.out.println(f.getName() + "\t" + fieldName.value() + "\t" + type + "\t"
                        + (notNull != null ? Boolean.TRUE : tab) + "\t");
            }
        }
    }

    private static String getFieldType(Field f) {

        String type = filterDot(f.getGenericType().toString());
        if (f.getType().isAssignableFrom(List.class)) {
            type = "List<" + type;
        }
        if (f.getType().isAssignableFrom(Map.class)) {
            String[] strs = f.getGenericType().toString().split(",");
            type = "Map<";
            for (String s : strs) {
                type += StringUtils.substringAfterLast(s, SEPARATOR) + ",";
            }

        }
        return type;
    }

    private static String filterDot(String s) {
        return s.indexOf(SEPARATOR) == -1 ? s : StringUtils.substringAfterLast(s, SEPARATOR);
    }




}
