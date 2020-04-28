package com.skymajo.libnetwork;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Type;

public class JsonConvert implements Convert {
    @Override
    public Object convert(String reponse, Type type) {
        JSONObject jsonObject = JSON.parseObject(reponse);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data != null) {
            Object dataObj = data.get("data");
            return JSON.parseObject(dataObj.toString(),type);
        }

        return null;
    }

    @Override
    public Object convert(String reponse, Class clz) {
        JSONObject jsonObject = JSON.parseObject(reponse);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data != null) {
            Object dataObj = data.get("data");
            return JSON.parseObject(dataObj.toString(),clz);
        }
        return null;
    }
}
