package com.skymajo.libnetcache

import android.util.Log
import com.alibaba.fastjson.JSON
import java.lang.reflect.Type

//import com.google.gson.Gson;
class JsonConvert<T>:Convert<T> {
    override fun convert(response: String, type: Type): T? {
        var jsonObject=JSON.parseObject(response)
        val data = jsonObject.getJSONObject("data")
        if(data!=null){
            val data1 = data?.get("data")
            return JSON.parseObject(data1.toString(),type)
        }
        return null
    }

    override fun convert(response: String, claz: Class<*>): T? {
        var jsonObject=JSON.parseObject(response)
        val data = jsonObject.getJSONObject("data")
        if(data!=null){
            val data1 = data?.get("data")
            return JSON.parseObject(data1.toString(),claz) as T
        }
        return null
    }
}