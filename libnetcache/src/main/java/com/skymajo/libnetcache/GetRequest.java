package com.skymajo.libnetcache;

public class GetRequest<T> extends Request<T,GetRequest> implements Cloneable{
    public GetRequest(String url) {
        super(url);
    }

    @Override
    protected okhttp3.Request generateRequest(okhttp3.Request.Builder builder) {
        okhttp3.Request request = builder.get().url(UrlCreator.createUrlFormParams(mUrl, params)).build();
        return request;
    }
}
