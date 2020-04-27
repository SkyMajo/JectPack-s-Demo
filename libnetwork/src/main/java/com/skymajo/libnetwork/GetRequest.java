package com.skymajo.libnetwork;

public class GetRequest<T> extends Request<T,GetRequest> {
    public GetRequest(String url) {
        super(url);
    }

    @Override
    protected okhttp3.Request generateRequest(okhttp3.Request.Builder builder) {
        okhttp3.Request request = builder.get().url(UrlCreator.createUrlFormParams(mUrl,params)).build();
        return request;
    }
}
