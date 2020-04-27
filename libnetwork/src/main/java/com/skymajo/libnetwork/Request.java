package com.skymajo.libnetwork;

import android.support.annotation.IntDef;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public abstract class Request<T,R> {
    protected String mUrl;
    protected HashMap<String,String> headers = new HashMap<>();
    protected HashMap<String,Object> params = new HashMap<>();

    //仅仅访问本地缓存，即便网络不存在也不会发起网络请求
    public static final int CACHE_ONLY = 1;
    //先访问缓存，同时进行网络请求，成功后缓存本地
    public static final int CACHE_FIRST = 2;
    //仅仅网络请求，不进行任何存储
    public static final int NET_ONLY = 3;
    //先访问网络，成功后访问缓存
    public static final int NET_CACHE = 4;
    private String cacheKey;

    @IntDef({CACHE_ONLY,CACHE_FIRST,NET_ONLY,NET_CACHE})
    public @interface CacheStrategy{}

    public Request(String url){
        this.mUrl = url;
    }

    public R addHeader(String key , String value){
        headers.put(key,value);
        return (R) this;
    }

    public R addParam(String key,Object value){
        try {
            Field field = value.getClass().getField("Type");
            Class clz = (Class) field.get(null);
            if (clz.isPrimitive()){
                params.put(key,value);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (R) this;
    }

    public R cacheKey(String key){
        this.cacheKey = key;
        return (R) this;
    }

    public void execute(JsonCallBack<T> callBack){
        getCall();
    }

    private Call getCall() {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        addHeaders(builder);
        okhttp3.Request request = generateRequest(builder);
        Call call = ApiServce.build.newCall(request);
        return call;
    }

    protected abstract okhttp3.Request generateRequest(okhttp3.Request.Builder builder);

    private void addHeaders(okhttp3.Request.Builder builder) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(),entry.getValue());
        }

    }


}
