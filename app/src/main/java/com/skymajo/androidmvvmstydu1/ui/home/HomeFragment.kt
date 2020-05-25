package com.skymajo.androidmvvmstydu1.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.skymajo.androidmvvmstydu1.AbsListFragment
import com.skymajo.androidmvvmstydu1.model.Feed
import com.skymajo.androidmvvmstydu1.ui.MuteableDataSource
import com.skymajo.libnavannotation.FragmentDestination


@FragmentDestination("home/home",false,true)
class HomeFragment : AbsListFragment<Feed, HomeViewModel>() {




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mViewModel.cacheLiveData.observe(this,
            Observer<PagedList<Feed>> {
                adapter.submitList(it)
            })
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getAdapter(): PagedListAdapter<*,*> {
        var feedType = if (arguments == null) {
            "all"
        } else {
            arguments!!.getString("feedType")
        }
        return  FeedAdapter(context,feedType)
    }


    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.dataSource.invalidate()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        var feed = adapter.currentList!![adapter.itemCount - 1]
        mViewModel.loadAfter(feed!!.id,object : ItemKeyedDataSource.LoadCallback<Feed>(){
            override fun onResult(data: MutableList<Feed>) {
                if (data != null && data.size>0) {
                    var config = adapter.currentList!!.config
                    var dataSource = MuteableDataSource<Int, Feed>()
                    dataSource.data.addAll(data)
                    var pagedList = dataSource.buildNewPageList(config)
                    submitList(pagedList)
                }
            }

        })
    }




}