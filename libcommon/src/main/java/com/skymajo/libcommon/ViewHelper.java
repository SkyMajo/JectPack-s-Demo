package com.skymajo.libcommon;

import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

public class ViewHelper {
    public static final int RADIUS_ALL = 0;
    public static final int RADIUS_LEFT = 1;
    public static final int RADIUS_TOP = 2;
    public static final int RADIUS_RIGHT = 3;
    public static final int RADIUS_BOTTOM = 4;

    public static void setViewOutLine(View view, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        TypedArray typedArray = view.getContext().obtainStyledAttributes(attrs, R.styleable.ViewOutLineStargy);
        int radius = typedArray.getDimensionPixelOffset(R.styleable.ViewOutLineStargy_radius, 0);
        int radiusSide = typedArray.getIndex(R.styleable.ViewOutLineStargy_radiusSide);
        typedArray.recycle();

        setViewOutLine(view, radius, radiusSide);
    }

    public static void setViewOutLine(View view, final int radius, final int radiusSide) {
        if (radius<=0) {
            return;
        }
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int width = view.getWidth();
                int height = view.getHeight();
                if (width <=0 && height<=0){
                    return;
                }

                if(radiusSide != RADIUS_ALL){
                    int left = 0 ,right = width,top = 0 ,bottom = height;
                    if(radiusSide <RADIUS_LEFT){
                        right += radius;
                    }else if(radiusSide == RADIUS_TOP){
                        bottom += radius;
                    }else if(radiusSide == RADIUS_RIGHT){
                        left -= radius;
                    }else if(radiusSide == RADIUS_BOTTOM){
                        top -= radius;
                    }
                    outline.setRoundRect(left,top,right,bottom,radius);
                }else{
                    if (radius>0) {
                        outline.setRoundRect(0,0,width,height,radius);
                    }else{
                        outline.setRect(0,0,width,height);
                    }
                }

            }
        });
    }
}
