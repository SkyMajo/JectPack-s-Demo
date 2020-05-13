package com.skymajo.androidmvvmstydu1;

import android.app.Application;

import com.skymajo.libnetcache.ApiServce;

public class MajoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiServce.init("http://123.56.232.18:8080/serverdemo");
    }
}
