package com.skymajo.androidmvvmstydu1.ui;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;

import java.nio.channels.AsynchronousCloseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.security.auth.callback.Callback;

public class MuteableDataSource<Key,Value> extends ItemKeyedDataSource<Key,Value> {


    public List<Value> data = new ArrayList<>();
    public PagedList<Value> buildNewPageList(PagedList.Config config){
        @SuppressLint("RestrictedApi") PagedList<Value> pagedList = new PagedList.Builder<Key,Value>(this,config)
                .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
                .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
                .build();
        return pagedList;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Key> params, @NonNull LoadInitialCallback<Value> callback) {
        callback.onResult(data);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Value> callback) {
        callback.onResult(Collections.emptyList());
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Value> callback) {
        callback.onResult(Collections.emptyList());
    }

    @NonNull
    @Override
    public Key getKey(@NonNull Value item) {
        return null;
    }
}
