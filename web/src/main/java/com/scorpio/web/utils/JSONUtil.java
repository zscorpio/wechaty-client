package com.scorpio.web.utils;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Scorpio
 */
public class JSONUtil {
    private JSONUtil(){}

    public static <T> T convert(Object base, Type type) {
        try {
            return JSON.parseObject(JSON.toJSONString(base), type);
        }catch (Exception e){
            return null;
        }
    }

    public static <T> T convertString(String base, Type type) {
        try {
            return JSON.parseObject(base, type);
        }catch (Exception e){
            return null;
        }
    }

    public static <T> List<T> convertList(String json, Class<T> clazz) {
        try {
            return JSON.parseArray(json, clazz);
        }catch (Exception e){
            return new ArrayList<>();
        }

    }
}
