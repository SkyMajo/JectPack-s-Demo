package com.skymajo.androidmvvmstydu1.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import com.alibaba.fastjson.TypeReference
import com.skymajo.androidmvvmstydu1.AbsViewModel
import com.skymajo.androidmvvmstydu1.model.Feed
import com.skymajo.androidmvvmstydu1.ui.MuteableDataSource
import com.skymajo.androidmvvmstydu1.ui.login.UserManager
import com.skymajo.libnetcache.ApiResponse
import com.skymajo.libnetcache.ApiServce
import com.skymajo.libnetcache.JsonCallBack
import com.skymajo.libnetcache.Request
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("UNCHECKED_CAST")
class HomeViewModel : AbsViewModel<Feed>() {


    @Volatile
    private var withCache = true

    private val loadAfter = AtomicBoolean(false)

    var cacheLiveData = MutableLiveData<PagedList<Feed>>()


    //DataSource<Key,Value> 数据源:Key对应加载数据对应的条件信息，Value对应数据实体类
    //Paging内置的DataSource ：
    //1.PageKeyedDataSource<key,value>:适用于目标数据根据页码信息请求数据的场景
    //2.ItemKeyedDataSource<Key,value>:适用于目标数据的加载依赖特定item的信息
    //3.PositionalDataSource<T>:适用于目标数据总数固定,通过特定的位置加载数据
    override fun createDataSource(): DataSource<*, *> {
        return dataSource
    }




    var dataSource:ItemKeyedDataSource<Int,Feed> = object :ItemKeyedDataSource<Int,Feed>() {
        override fun loadInitial(
            params: LoadInitialParams<Int>,

            callback: LoadInitialCallback<Feed>) {
            //做加载初始化数据
            //首页网络数据请求
            loadData(0,callback)
            withCache = false
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Feed>) {
            //做加载分页数据
            loadData(params.key,callback)
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Feed>) {
            callback.onResult(Collections.emptyList())
            //能够向前加载数据的
        }

        override fun getKey(item: Feed): Int {
            //通过最后一条数据来返回一个id
            return item.id

        }

    }

    private fun loadData(key: Int, callback: ItemKeyedDataSource.LoadCallback<Feed>) {
        ///feeds/queryHotFeedsList
        if (key > 0) {
            loadAfter.set(true)
        }
        val request = ApiServce.get<Any>("/feeds/queryHotFeedsList")
            .addParam("feedType", null)
            .addParam("userId", UserManager.get().userId)
            .addParam("feedId", key)
            .addParam("pageCount", 10)
            .responseType(object : TypeReference<ArrayList<Feed>>() {}.type)
        if (withCache) {
            request.cacheStrategy(Request.CACHE_ONLY)
            request.execute(object : JsonCallBack<List<Feed>>() {
                override fun onCacheSuccess(response: ApiResponse<List<Feed>>?) {
                    super.onCacheSuccess(response)
                    if(response?.body == null){
                        Log.e("onCacheSuccess","onCacheSuccess:null")
                        withCache = false
                    }else{
                    val body = response.body
                    val dataSource =  MuteableDataSource<Int,Feed>()
                    dataSource.data.addAll(body!!)
                    val buildNewPageList = dataSource.buildNewPageList(config)
                    cacheLiveData.postValue(buildNewPageList)
                    }
                }
            })
        }

        try{
            val newRequest=if(withCache) { request.clone() } else request
            newRequest.cacheStrategy (if (key==0){Request.NET_CACHE}else{Request.NET_ONLY})
            val reponse = newRequest.execute()
            val data:List<Feed> = if (reponse.body == null) {
                Collections.emptyList<Any>()
            }else{
                reponse.body
            } as List<Feed>
            Log.e("result","${reponse.body}")
            callback.onResult(data)
            if(key>0){
                //通过liveData发送数据 告诉UI层 是否应该主动关闭上拉加载分页的动画
                mutableLiveData.postValue(data.isNotEmpty())
                loadAfter.set(false)
            }
        }catch (e:CloneNotSupportedException){

        }

    }
    @SuppressLint("RestrictedApi")
    fun loadAfter(
        id: Int,
        callBack: ItemKeyedDataSource.LoadCallback<Feed>
    ) {
        if (loadAfter.get()){
            callBack.onResult(Collections.emptyList())
            return
        }
        ArchTaskExecutor.getIOThreadExecutor().execute{
            kotlin.run {
                loadData(id,callBack)
            }
        }
    }


}