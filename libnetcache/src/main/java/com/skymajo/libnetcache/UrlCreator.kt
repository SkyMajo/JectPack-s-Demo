package com.skymajo.libnetcache

import android.util.Log
import java.lang.StringBuilder
import java.net.URLEncoder

object UrlCreator {
    fun creatUrlFromParams(url:String,params:Map<String,Any>):String{
        if( params.isEmpty()) return url
        var builder= StringBuilder()
        builder.append(url)
        Log.e("UrlCreator","${url.indexOf("?")>0 || url.indexOf("&") >0}")
        if(url.indexOf("?")>0 || url.indexOf("&") >0){
            Log.e("UrlCreator","")
            builder.append("&")
        }else {
            builder.append("?")
        }
        for((key,value) in params){
            var encodeValue=URLEncoder.encode(value.toString(),"UTF-8")
            builder.append(key).append("=").append(encodeValue).append("&")

        }
        builder.deleteCharAt(builder.length-1)
        Log.e("UrlCreator","${builder.toString()}")
        return builder.toString()
    }
}