package com.skymajo.androidmvvmstydu1.utils;

import android.util.DisplayMetrics;

public class PixUtils {

    public static int dp2px (int dpValue){
        DisplayMetrics metrics = AppGlobals.INSTANCE.getApplication().getResources().getDisplayMetrics();
        return (int) (metrics.density*dpValue+0.5f);
    }

    public static int getScreemWidth(){
        DisplayMetrics metrics = AppGlobals.INSTANCE.getApplication().getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreemHeight(){
        DisplayMetrics metrics = AppGlobals.INSTANCE.getApplication().getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

}
