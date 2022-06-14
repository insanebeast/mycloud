package com.pueeo.im.netty.protocol;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
@Data
public class JsonMsg {
    //devId Field(域)
    private int id;
    //content Field(域)
    private String content = "疯狂创客圈-Java高并发社群";

    //在通用方法中，使用阿里FastJson转成Java对象
    public static JsonMsg parseFromJson(String json) {
        return JSONObject.parseObject(json, JsonMsg.class);
    }

    //在通用方法中，使用谷歌Gson转成字符串
    public String convertToJson() {
        return JSONObject.toJSONString(this);
    }
    public JsonMsg(int id) {
        this.id = id;
    }

    public JsonMsg() {
        this.id = RandomUtil.randomInt(1,100);
    }

}
