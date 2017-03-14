package com.ly.common.utils.json;

import org.junit.Test;

import java.util.Date;

/**
 * Created by lixixi on 2016/10/14.
 */
public class JsonUtilTest {

    @Test
    public void dataParseTest() {
        System.out.println(JsonUtil.string2Object("\"2016-09-09 12:21:21\"", Date.class));
        System.out.println(JsonUtil.string2Object(String.valueOf(new Date().getTime()), Date.class));
        System.out.println(JsonUtil.string2Object("\"" + String.valueOf(new Date().getTime()) + "\"", Date.class));
        System.out.println(JsonUtil.string2Object("\"\"" + String.valueOf(new Date().getTime()) + "\"\"", Date.class));
        System.out.println(JsonUtil.string2Object("2016-09-09 12:21:21", Date.class));
    }
}
