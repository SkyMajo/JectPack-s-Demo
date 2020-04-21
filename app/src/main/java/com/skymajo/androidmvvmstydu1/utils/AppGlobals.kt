package com.skymajo.androidmvvmstydu1.utils

import android.app.Application

object AppGlobals {

    private var application:Application? = null

    fun getApplication():Application?{
        if (application == null){
            var method =
                Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication")
            application = method.invoke(null, null) as Application
        }
        return application
    }

}