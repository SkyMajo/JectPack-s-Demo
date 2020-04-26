package com.skymajo.androidmvvmstydu1.utils

import android.app.Application
import java.lang.reflect.InvocationTargetException

object AppGlobals {

    private var application:Application? = null

    fun getApplication():Application?{
        if (application == null){
            try {
                application = Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication")
                    .invoke(null) as Application
            } catch (e:IllegalAccessException ) {
                e.printStackTrace();
            } catch (e: InvocationTargetException) {
                e.printStackTrace();
            } catch (e:NoSuchMethodException) {
                e.printStackTrace();
            } catch (e:ClassNotFoundException) {
                e.printStackTrace();
            }

        }


        return application
    }

}