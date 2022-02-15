package com.pongsky.repository.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Json工具
 *
 * @author pengsenhao
 * @create 2019-10-21 9:55
 */
public class JsonUtils {

    /**
     * object转string，优化 List、时间格式
     *
     * @param object object
     * @return string
     */
    public static String toJsonString(Object object) {
        return JSON.toJSONString(object,
                // 输出值为null的字段
                SerializerFeature.WriteMapNullValue,
                // List字段如果为null,输出为[],而非null
                SerializerFeature.WriteNullListAsEmpty,
                // 字段按照TreeMap排序
                SerializerFeature.MapSortField,
                // 全局修改日期格式
                SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 将 微信 object 转 json 字符串
     *
     * @param obj 微信信息
     * @return 将 微信 object 转 json 字符串
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-02-09 12:36 下午
     */
    public static String toJson(Object obj) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        return gson.toJson(obj);
    }

}
