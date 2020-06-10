package com.skymajo.libnetcache;

import android.util.Log;

public class GetRequest<T> extends Request<T,GetRequest> implements Cloneable{
    public GetRequest(String url) {
        super(url);
    }

    @Override
    protected okhttp3.Request generateRequest(okhttp3.Request.Builder builder) {
        Log.e("GetRequest","走了--"+"Url:"+mUrl+"params"+params);
        okhttp3.Request request = builder.get().url(UrlCreator.INSTANCE.creatUrlFromParams(mUrl, params)).build();
        Log.e("GetRequest","走了--"+request.url());
        return request;
    }
}
