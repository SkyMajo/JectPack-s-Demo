package com.skymajo.androidmvvmstydu1.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import com.alibaba.fastjson.TypeReference
import com.skymajo.androidmvvmstydu1.AbsViewModel
import com.skymajo.androidmvvmstydu1.model.Feed
import com.skymajo.libnetcache.ApiResponse
import com.skymajo.libnetcache.ApiServce
import com.skymajo.libnetcache.JsonCallBack
import com.skymajo.libnetcache.Request
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel : AbsViewModel<Feed>() {


    @Volatile
    private var withCache = true


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
            Log.e("ItemKeyedDataSource:","走了$withCache")
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
        var request = ApiServce.get<Any>("/feeds/queryHotFeedsList")
            .addParam("feedType", null)
            .addParam("userId", 0)
            .addParam("feedId", key)
            .addParam("pageCount", 10)
            .responseType(object : TypeReference<ArrayList<Feed>>() {}.type)
        if (withCache) {
            request.cacheStrategy(Request.CACHE_ONLY)
            request.execute(object : JsonCallBack<List<Feed>>() {
                override fun onCacheSuccess(response: ApiResponse<List<Feed>>?) {
                    super.onCacheSuccess(response)
                    Log.e("onCacheSuccess","onCacheSuccessD${response.toString()}")
                    var body = response?.body
                    if(response?.body == null){
                        withCache = false
                    }

                }
            })
        }

        try{
            var newRequest=if(withCache) { request.clone() } else request
//            var netReqeust = if (withCache){request}else{request}
//            var netReqeust = if (withCache){request}else{request}
            newRequest.cacheStrategy (if (key==0){Request.NET_CACHE}else{Request.NET_ONLY})
            var reponse = newRequest.exqueue()
            var data:List<Feed> = if (reponse.body == null) {
                Collections.emptyList<Any>()
            }else{
                reponse.body
            } as List<Feed>
            Log.e("result","${reponse.body}")
            callback.onResult(data)

            if(key>0){
                //通过LiveData发送数据 告诉UI层 是否应该主动关闭上拉加载分页的动画
                mutableLiveData.postValue(data.isNotEmpty())
            }
        }catch (e:CloneNotSupportedException){

        }

    }

}