package com.skymajo.androidmvvmstydu1;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public abstract class AbsViewModel<T> extends ViewModel {

    private DataSource dataSource;
    private LiveData<PagedList<T>> pagedData;
    private MutableLiveData<Boolean> mutableLiveData = new MutableLiveData<>();

    public AbsViewModel(){
        PagedList.Config config = new PagedList
                .Config.Builder()
                .setPageSize(10)//一页的内容条数
                .setInitialLoadSizeHint(12)//一次加载多少条
//                .setMaxSize(100) 一般用不到
//                .setEnablePlaceholders(false)//占位符
//                    .setPrefetchDistance() //距离屏幕底部还有多少条的时候加载下一页数据
                .build();

        pagedData = new LivePagedListBuilder<>(factory, config)
                .setInitialLoadKey(0)//初始化时加载的参数
//                .setFetchExecutor()//传入线程池，用来异步调用，不需要传入，paging有默认的线程池
                .setBoundaryCallback(callback)//这个CallBack可以监听到数据加载的状态,有状态之后可以知道，界面是否有数据，已加载数据。
                .build();
    }

    public LiveData<PagedList<T>> getData(){
        return pagedData;
    }

    public DataSource getDataSource(){
        return dataSource;
    }

    public MutableLiveData<Boolean> getMutableLiveData() {
        return mutableLiveData;
    }

    PagedList.BoundaryCallback<T> callback = new PagedList.BoundaryCallback<T>() {
        @Override
        public void onZeroItemsLoaded() {
            //加载数据初始化的时候，返回了空值，走该回调
            //可以在这里来展示空布局
//            super.onZeroItemsLoaded();

            mutableLiveData.postValue(false);
        }

        @Override
        public void onItemAtFrontLoaded(@NonNull T itemAtFront) {
            //第一条数据被加载时，会走这个回调
//            super.onItemAtFrontLoaded(itemAtFront);
            mutableLiveData.postValue(true);
        }

        @Override
        public void onItemAtEndLoaded(@NonNull T itemAtEnd) {
            //最后一条数据加载的时候，会回调到这里
            super.onItemAtEndLoaded(itemAtEnd);
        }
    };

    final DataSource.Factory factory = new DataSource.Factory() {
        @NonNull
        @Override
        public DataSource create() {
            dataSource = createDataSource();
            return dataSource;
        }
    };

    public abstract DataSource createDataSource();

}
