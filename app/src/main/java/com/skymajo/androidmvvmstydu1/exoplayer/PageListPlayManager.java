package com.skymajo.androidmvvmstydu1.exoplayer;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSinkFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.skymajo.androidmvvmstydu1.utils.AppGlobals;

import java.util.HashMap;

public class PageListPlayManager {

    private static HashMap<String,PageListPlay> sPageListPlayHashMap = new HashMap<>();

    private static final ProgressiveMediaSource.Factory mediaSourceFractory ;


    //总体流程，当我们传入视频url的时候，DataSourceFactory
    // 先通过CacheDataSourceFactory查询是否有缓存文件
    //如果有就通过拿缓存播放，
    //如果没有就通过HttpDataSourceFactory去下载视频文件下载到cacheDir
    //然后在通过缓存去播放，就实现了边播变缓存。
    static {
        //构建DefaultDataSourceFactory用于视频下载
        //DefaultDataSourceFactory可以根据视频URL下载视频
        Application application = AppGlobals.INSTANCE.getApplication();
        String packageName = application.getPackageName();
        Context applicationContext = application.getApplicationContext();
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(applicationContext, Util.getUserAgent(applicationContext, packageName));

        //SimpleCache(地址，策略) 用于视频缓存
        Cache cache = new SimpleCache(application.getCacheDir(),new LeastRecentlyUsedCacheEvictor(200*1024*1024));
        //用于视频写入本地缓存
        CacheDataSinkFactory cacheDataSinkFactory = new CacheDataSinkFactory(cache,Long.MAX_VALUE);

        //查询缓存文件的工厂类CacheDataSourceFactory（缓存，文件流，文件工厂，缓存写入工厂类，缓存文件的策略(例如并发时的策略)）
        CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(cache, defaultDataSourceFactory, new FileDataSourceFactory(), cacheDataSinkFactory, CacheDataSource.FLAG_BLOCK_ON_CACHE, null);

        //MediaSourceFractory 供给播放器使用
       mediaSourceFractory = new ProgressiveMediaSource.Factory(cacheDataSourceFactory);

    }

    //创建MediaSource数据源的方法
    public static MediaSource createMediaSource(String url){
        return mediaSourceFractory.createMediaSource(Uri.parse(url));
    }



    public static PageListPlay get(String pageName){
        PageListPlay pageListPlay = sPageListPlayHashMap.get(pageName);
        if (pageListPlay == null) {
            pageListPlay = new PageListPlay();
            sPageListPlayHashMap.put(pageName,pageListPlay);
        }
        return pageListPlay;
    }


    public static void release(String pageName){
        PageListPlay pageListPlay = sPageListPlayHashMap.get(pageName);
        if (pageListPlay != null) {
            pageListPlay.release();
        }
    }

}
