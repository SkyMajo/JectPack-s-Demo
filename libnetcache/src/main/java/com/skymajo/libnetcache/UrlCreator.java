package com.skymajo.libnetcache;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class UrlCreator {
    public static String createUrlFormParams(String url, Map<String , Object> params){
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        if (url.indexOf("?")>0 || url.indexOf("&")>0){
            builder.append("&");
        }else{
            builder.append("?");
        }
        for (Map.Entry<String,Object>entry:params.entrySet()){
            String value = null;
            try {
                value = URLEncoder.encode(String.valueOf(entry.getValue()),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            builder.append(entry.getKey()).append("=").append(value).append("&");
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }
}
