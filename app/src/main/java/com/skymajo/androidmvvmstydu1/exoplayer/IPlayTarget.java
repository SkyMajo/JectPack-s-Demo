package com.skymajo.androidmvvmstydu1.exoplayer;

import android.view.ViewGroup;

public interface IPlayTarget {

    //拿到容器，拿到View才能知道滚动位置
    ViewGroup getOwner();

    //如果满足播放需求，调用::onActivity进行播放
    void onActivity();

    //滚出屏幕，调用::inActivity()停止播放
    void inActivity();

    boolean isPlaying();

}
