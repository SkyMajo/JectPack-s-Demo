package com.skymajo.libnetcache;


import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;

import com.skymajo.libnetcache.cache.CacheManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public abstract class Request<T,R> implements Cloneable {
    protected String mUrl;
    private Class mClaz;
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
    private Type mType;
    private Class mClz;
    private int mCacheStrategy;

    @IntDef({CACHE_ONLY,CACHE_FIRST,NET_ONLY,NET_CACHE})
    public @interface CacheStrategy{}

    public Request(String url){
        this.mUrl = url;
    }

    public R addHeader(String key , String value){
        headers.put(key,value);
        return (R) this;
    }

    public R addParam(String key, Object value) {
        if (value == null) {
            return (R) this;
        }
        //int string byte char
        try {
            Field field = value.getClass().getField("TYPE");
            Class claz = (Class) field.get(null);
            if (claz.isPrimitive()) {
                params.put(key, value);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (R) this;
    }


    public R cacheStrategy(@CacheStrategy int cacheStrategy){
        mCacheStrategy = cacheStrategy;
        return (R) this;
    }

    public R cacheKey(String key){
        this.cacheKey = key;
        return (R) this;
    }

    @SuppressLint("RestrictedApi")
    public void execute(final JsonCallBack<T> callBack){


        if(mCacheStrategy != NET_ONLY){
            ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    ApiResponse<T> response = readCache();
                    if (callBack!=null) {
                        callBack.onCacheSuccess(response);
                    }
                }
            });
        }
       if (mCacheStrategy != CACHE_ONLY){
           getCall().enqueue(new Callback() {
               @Override
               public void onFailure(@NotNull Call call, @NotNull IOException e) {
                   ApiResponse<T> response = new ApiResponse<>();
                   response.message = e.getMessage();
                   callBack.onError(response);

               }

               @Override
               public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                   ApiResponse<T> apiResponse = parseResponse(response,callBack);
                   if (apiResponse.success) {
                       callBack.onSusccess(apiResponse);
                   }else{
                       callBack.onError(apiResponse);
                   }
               }
           });
       }
    }

    private ApiResponse<T> readCache() {
        String key = TextUtils.isEmpty(cacheKey)?createCacheKey():cacheKey;
        Object cache = CacheManager.getCache(key);
        ApiResponse<T> response = new ApiResponse<>();
        response.body = (T) cache;
        response.status = 304;
        response.message = "获取缓存成功";
        response.success = true;
        return response;
    }


    public  R responseType(Type type){
        mType = type;
        return (R) this;
    }
    public  R responseType(Class clz){
        mClz = clz;
        return (R) this;
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

    public ApiResponse<T> exqueue() {
        if (mType == null && mClaz == null) {
            throw new RuntimeException("同步方法,type|classType 类型必须设置");
        }

        if (mCacheStrategy == CACHE_ONLY) {
            return readCache();
        }

        if (mCacheStrategy != CACHE_ONLY) {
            ApiResponse<T> result = null;
            try {
                Response response = getCall().execute();
                result = parseResponse(response, null);
            } catch (IOException e) {
                e.printStackTrace();
                if (result == null) {
                    result = new ApiResponse<>();
                    result.message = e.getMessage();
                }
            }
            return result;
        }
        return null;
    }



    private ApiResponse<T> parseResponse(Response response, JsonCallBack<T> callBack) {
        String message = "";
        int status = response.code();
        boolean success = response.isSuccessful();
        ApiResponse<T> result = new ApiResponse<>();
        Convert convert = ApiServce.sConvert;

        try{
            String content = response.body().string();

            if (success){
                if (callBack != null) {
                    ParameterizedType type = (ParameterizedType) callBack.getClass().getGenericSuperclass();
                    Type argument = type.getActualTypeArguments()[0];
                    result.body = (T) convert.convert(content,argument);
                }else if (mType != null){
                    result.body = (T) convert.convert(content,mType);
                }else if(mClz != null){
                    result.body = (T) convert.convert(content,mClz);
                }else{
                    System.out.println("Request:"+"无法解析该类型。");
                }
            }else{
                message = content;
            }
        } catch (IOException e) {
            message = e.getMessage();
            success = false;
            e.printStackTrace();
        }
        result.success = success;
        result.message = message;
        result.status = status;

        if (mCacheStrategy != NET_ONLY && result.success && result.body!=null && result.body instanceof Serializable){
            saveCache(result.body);
        }


        return result;
    }

    private T saveCache(T body){
        String key = TextUtils.isEmpty(cacheKey)?createCacheKey():cacheKey;
        CacheManager.save(key,body);
        return (T) this;
    }

    private String createCacheKey() {
        cacheKey = UrlCreator.createUrlFormParams(mUrl,params);
        return cacheKey;
    }

    @NonNull
    @Override
    public Request clone() throws CloneNotSupportedException {
        return (Request<T, R>) super.clone();
    }

}
