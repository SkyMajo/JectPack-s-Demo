package com.skymajo.androidmvvmstydu1.exoplayer;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.skymajo.androidmvvmstydu1.R;
import com.skymajo.androidmvvmstydu1.utils.AppGlobals;

public class PageListPlay {

    public   SimpleExoPlayer exolayer;
    public   PlayerView player;
    public   PlayerControlView controlView;
    private boolean isPlaying;
    public  String playUrl;


    public PageListPlay() {
        Application application = AppGlobals.INSTANCE.getApplication();
        exolayer = ExoPlayerFactory.newSimpleInstance(
                application,
                new DefaultRenderersFactory(application),
                new DefaultTrackSelector(),
                new DefaultLoadControl());

        player = (PlayerView) LayoutInflater.from(application).inflate(R.layout.layout_exo_player_view, null, false);
        controlView = (PlayerControlView) LayoutInflater.from(application).inflate(R.layout.layout_exo_player_contorller_view, null, false);

        player.setPlayer(exolayer);
        controlView.setPlayer(exolayer);
    }

    public void release() {
        if (exolayer != null){
            exolayer.setPlayWhenReady(false);
            exolayer.stop(true);
            exolayer.release();
            exolayer = null;
        }

        if(player != null){
            player.setPlayer(null);
            player = null;
        }
        if (controlView != null){
            controlView.setPlayer(null);
            controlView.setVisibilityListener(null);
            controlView = null;
        }

    }

}
