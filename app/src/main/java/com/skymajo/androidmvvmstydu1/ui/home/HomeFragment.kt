package com.skymajo.androidmvvmstydu1.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedListAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.skymajo.androidmvvmstydu1.AbsListFragment
import com.skymajo.androidmvvmstydu1.R
import com.skymajo.androidmvvmstydu1.model.Feed
import com.skymajo.libnavannotation.FragmentDestination


@FragmentDestination("home/home",false,true)
class HomeFragment : AbsListFragment<Feed, HomeViewModel>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }




}