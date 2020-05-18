package com.skymajo.libnetcache

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.lang.reflect.Type

class JsonConvert<T>:Convert<T> {
    override fun convert(response: String, type: Type): T? {
        var jsonObject= Gson().fromJson(response,JsonObject::class.java)
        val data = jsonObject.getAsJsonObject("data")
        if(data!=null){
            return Gson().fromJson(data.get("data").toString(),type)
        }
        return null
    }

    override fun convert(response: String, claz: Class<*>): T? {
        var jsonObject=Gson().fromJson(response,JsonObject::class.java)
        var data=jsonObject.getAsJsonObject("data")
        data?.run {
            return Gson().fromJson(data["data"].toString(),claz) as T
        }
        return null
    }
}