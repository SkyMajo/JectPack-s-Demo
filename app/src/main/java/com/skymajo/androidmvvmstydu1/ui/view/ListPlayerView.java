package com.skymajo.androidmvvmstydu1.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.skymajo.androidmvvmstydu1.R;
import com.skymajo.androidmvvmstydu1.exoplayer.IPlayTarget;
import com.skymajo.androidmvvmstydu1.exoplayer.PageListPlay;
import com.skymajo.androidmvvmstydu1.exoplayer.PageListPlayManager;
import com.skymajo.androidmvvmstydu1.utils.PixUtils;
import com.skymajo.androidmvvmstydu1.view.MajoImageView;

public class ListPlayerView extends FrameLayout implements IPlayTarget, PlayerControlView.VisibilityListener, Player.EventListener {

    private View bufferView ;
    private MajoImageView cover,blur;
    private ImageView playBtn;
    private String category;
    private String vedioUrl;
    private boolean isPlaying;

    public ListPlayerView(@NonNull Context context) {
        this(context,null);
    }

    public ListPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ListPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_player_view,this,true);

        bufferView = findViewById(R.id.buffer_view);
        cover = findViewById(R.id.cover);
        blur = findViewById(R.id.blur_background);
        playBtn = findViewById(R.id.play_btn);

        playBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying()) {
                    inActivity();
                }else{
                    onActivity();
                }
            }
        });

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PageListPlay pageListPlay = PageListPlayManager.get(category);
        pageListPlay.controlView.show();
        return super.onTouchEvent(event);
    }

    public void bindData(String category, int widthPx , int heightPx, String coverUrl, String vedioUrl){
        this.category = category;
        this.vedioUrl = vedioUrl;

        cover.setImageUrl(cover,coverUrl,false);
        if (widthPx<heightPx){
            blur.setBlurImageUrl(coverUrl,10);
            blur.setVisibility(VISIBLE);
        }else{
            blur.setVisibility(INVISIBLE);
        }
        setSize(widthPx,heightPx);
    }

    protected void setSize(int widthPx, int heightPx) {
        int maxWidth = PixUtils.getScreemWidth();
        int maxHeight = maxWidth;
        int layoutWidth = maxWidth;
        int layoutHeight = 0;
        int coverWidth;
        int coverHeight;
        if(widthPx>=heightPx){
            coverWidth = maxWidth;
            layoutHeight = coverHeight = (int) (heightPx/(widthPx*1.0f/maxWidth));
        }else{
            layoutHeight = coverHeight = maxHeight;
            coverWidth = (int) (widthPx/(heightPx*1.0f/maxHeight));
        }
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = layoutWidth;
        params.height = layoutHeight;
        setLayoutParams(params);

        ViewGroup.LayoutParams blurParams = blur.getLayoutParams();
        blurParams.width = layoutWidth;
        blurParams.height = layoutHeight;
        blur.setLayoutParams(blurParams);


        FrameLayout.LayoutParams coverLayoutParams = (LayoutParams) cover.getLayoutParams();
        coverLayoutParams.width = coverWidth;
        coverLayoutParams.height = coverHeight;
        coverLayoutParams.gravity = Gravity.CENTER;
        cover.setLayoutParams(coverLayoutParams);


        FrameLayout.LayoutParams playParams = (LayoutParams) playBtn.getLayoutParams();
        playParams.gravity = Gravity.CENTER;
        playBtn.setLayoutParams(playParams);

    }


    @Override
    public ViewGroup getOwner() {
        return this;
    }

    @Override
    public void onActivity() {
        PageListPlay pageListPlay = PageListPlayManager.get(category);
        PlayerView player = pageListPlay.player;
        PlayerControlView controlView = pageListPlay.controlView;
        SimpleExoPlayer exolayer = pageListPlay.exolayer;
        //获取playerView的父容器
        ViewParent parent = player.getParent();
        if (parent!=this) {
            //如果说父容器不是当前ListPlayerView本身的话，就把PlayerView添加进来
            //添加之前首先要判断父容器是不是空，如果不是空说明已经被添加到别人里面去了，我们要先移除掉才行
            if (parent!=null){
                ((ViewGroup)parent).removeView(player);
            }
            //确保以上条件后，调用.addView添加进去
            //index参数是顺序，因为高斯模糊背景图在最底层，所以视频要放在他的上面，所以给他个1
            //获取封面图宽高，视频播放器的大小应该跟封面图一样，所以直接设置封面图的参数就行了
            ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
            this.addView(player,1,layoutParams);
        }

        //设置控制器
        ViewParent ctrlParent = controlView.getParent();
        if (ctrlParent !=  this) {
            if (ctrlParent!=null){
                ((ViewGroup)ctrlParent).removeView(controlView);
            }
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.BOTTOM;
            this.addView(controlView,params);
            controlView.setVisibilityListener(this);
        }
        controlView.show();

        //判断是否是同一个视频资源
        if (TextUtils.equals(pageListPlay.playUrl,vedioUrl)){

        }else{
            MediaSource mediaSource = PageListPlayManager.createMediaSource(vedioUrl);
            exolayer.prepare(mediaSource);
            //无线循环播放
            exolayer.setRepeatMode(Player.REPEAT_MODE_ONE);
            exolayer.addListener(this);
        }
        exolayer.setPlayWhenReady(true);

    }


    /***
     * 防止复用问题，复写$onAttachedToWindow方法，进行一些数据重置
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isPlaying = false;
        bufferView.setVisibility(GONE);
        cover.setVisibility(VISIBLE);
        playBtn.setVisibility(VISIBLE);
        playBtn.setImageResource(R.drawable.icon_video_play);
    }

    @Override
    public void inActivity() {
        PageListPlay pageListPlay = PageListPlayManager.get(category);
        pageListPlay.exolayer.setPlayWhenReady(false);
        playBtn.setVisibility(VISIBLE);
        playBtn.setImageResource(R.drawable.icon_video_play);
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void onVisibilityChange(int visibility) {
        playBtn.setVisibility(visibility);
        playBtn.setImageResource(isPlaying()?R.drawable.icon_video_pause:R.drawable.icon_video_play);

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        PageListPlay pageListPlay = PageListPlayManager.get(category);
        SimpleExoPlayer exolayer = pageListPlay.exolayer;
        //判断视频是否已经开始播放了（播放器状态为准备播放同时缓冲区有数据）
        if (playbackState == Player.STATE_READY && exolayer.getBufferedPosition()!=0){
            //已经播放，隐藏封面图
            cover.setVisibility(INVISIBLE);
            bufferView.setVisibility(INVISIBLE);
        }else if(playbackState == Player.STATE_BUFFERING){
            //缓冲
            bufferView.setVisibility(VISIBLE);
        }
       isPlaying = playbackState == Player.STATE_READY && exolayer.getBufferedPosition()!=0 &&playWhenReady;
        playBtn.setImageResource(isPlaying()?R.drawable.icon_video_pause:R.drawable.icon_video_play);
    }
}
