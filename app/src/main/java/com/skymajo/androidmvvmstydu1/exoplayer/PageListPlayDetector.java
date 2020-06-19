package com.skymajo.androidmvvmstydu1.exoplayer;

import android.graphics.Point;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PageListPlayDetector {


    private List<IPlayTarget> mTargets = new ArrayList<>();
    private RecyclerView recyclerView;
    private IPlayTarget mPlayingTarget;


    public void addTarget(IPlayTarget target){
        mTargets.add(target);
    }


    public void removeTarget(IPlayTarget target){
        mTargets.remove(target);
    }

    public PageListPlayDetector(LifecycleOwner owner , RecyclerView recyclerView){
        this.recyclerView = recyclerView;

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
                //滚动时是否满足自动播放逻辑，如果不满足了，就要停止播放
                if (mPlayingTarget != null && mPlayingTarget.isPlaying() && !isTargetInBounds(mPlayingTarget)) {
                   mPlayingTarget.inActivity();
                }
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


    //playview有一半划入屏幕，就算自动播放
    //所以我们要拿到playerView，才能知道滑出屏幕多少
    //所以需要创建一个接口，通过接口切面传递过来
    private void autoPlay() {

        //判断是否满足自动播放的条件
        //1.首先判断屏幕内容上是否已经有视频类型的itemPlayTarget已经添加到我们的集合里面
        //2.判断RecyclerView的child是否>0
        if (mTargets.size()<=0 || recyclerView.getChildCount()<=0){
            return;
        }

        //判断上一个playTarget是否还满足条件，如果还满足条件就没必要创造新的playTarget了
        if (mPlayingTarget != null && mPlayingTarget.isPlaying() && isTargetInBounds(mPlayingTarget)) {
            return;
        }



        //其次遍历两个集合
        IPlayTarget activeTarget = null;
        for (IPlayTarget mTarget : mTargets) {
            //检测PlayeTarget是否有一半的比例在屏幕内
            boolean targetInBounds = isTargetInBounds(mTarget);
            if (targetInBounds){
                //满足自动播放，跳出循环，开始准备播放
                activeTarget = mTarget;
                break;
            }

        }
        if (activeTarget != null){
            //当新的playTarget满足条件后，将上一个playtarget关闭掉
            if (mPlayingTarget != null && mPlayingTarget.isPlaying()) {
                mPlayingTarget.inActivity();
            }
             mPlayingTarget = activeTarget;
            activeTarget.onActivity();
        }


    }

    private boolean isTargetInBounds(IPlayTarget mTarget) {
        //获得playTarget所在的容器
        ViewGroup owner = mTarget.getOwner();
        //获取RecyclerView的中心位置
        ensureRecyclerViewLocation();
        //如果这个容器没显示，或者不在windows上
        if (!owner.isShown() || !owner.isAttachedToWindow()){
            return false;
        }
        //否则就应该计算这个owner在屏幕上所处的位置
        int[] location = new int[2];
        owner.getLocationOnScreen(location);
        //计算owner的中心值 :: center = owner.y + (owner.height/2)
        int center = location[1]+owner.getHeight()/2;
        return center>=rvLocation.x && center<=rvLocation.y;
    }

    private Point rvLocation = null;

    //获取RecyclerView的中心位置
    private Point ensureRecyclerViewLocation() {
        if (rvLocation == null){
            //得到RecyclerView的X&Y
            int[] location = new int[2];
            recyclerView.getLocationOnScreen(location);

            int top = location[1];
            int bottom = top+recyclerView.getHeight();
            rvLocation =  new Point(top,bottom);
        }
        return rvLocation;
    }

    public void onResume(){
        if (mPlayingTarget != null) {
            mPlayingTarget.onActivity();
        }
    }

    public void onPause(){
        if (mPlayingTarget != null) {
            mPlayingTarget.inActivity();
        }
    }


}
