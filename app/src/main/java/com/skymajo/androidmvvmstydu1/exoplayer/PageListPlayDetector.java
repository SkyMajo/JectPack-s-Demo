package com.skymajo.androidmvvmstydu1.exoplayer;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PageListPlayDetector {


    private List<IPlayTarget> mTargets = new ArrayList<>();


    public void addTarget(IPlayTarget target){
        mTargets.add(target);
    }


    public void removeTarget(IPlayTarget target){
        mTargets.remove(target);
    }

    public PageListPlayDetector(LifecycleOwner owner , RecyclerView recyclerView){

        owner.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                //宿主每一个生命周期的变更，都会回调到onStateChanged里面
                if (event == Lifecycle.Event.ON_DESTROY) {
                    //Destory生命周期状态
                    recyclerView.getAdapter().unregisterAdapterDataObserver(mDataObserver);
                    owner.getLifecycle().removeObserver(this);
                }
            }
        });


        recyclerView.getAdapter().registerAdapterDataObserver(mDataObserver);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //滚动停止，自动播放，说明当前用户在盯着一个item
                    autoPlay();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {

        //当与数据被添加到RecyclerView中，就会回调RecyclerView.AdapterDataObserver::onItemRangeInserted方法
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            autoPlay();
        }
    };

    private void autoPlay() {
        //playview有一半划入屏幕，就算自动播放
        //所以我们要拿到playerView，才能知道滑出屏幕多少
        //所以需要创建一个接口，通过接口切面传递过来

    }


}
