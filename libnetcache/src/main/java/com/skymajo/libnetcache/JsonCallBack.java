package com.skymajo.libnetcache;

import com.skymajo.libnetcache.ApiResponse;

public abstract class JsonCallBack <T> {
    public void onSusccess(ApiResponse<T> response){

    }

    public void onError(ApiResponse<T> response){

    }

    public void onCacheSuccess(ApiResponse<T> response){

    }
}
