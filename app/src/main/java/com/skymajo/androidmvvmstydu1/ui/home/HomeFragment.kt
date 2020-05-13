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



    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.data.observe(viewLifecycleOwner,Observer{
//            textView.text = it.get(0)!!.activityText
        })
//        ViewModel.dataSource.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }



    override fun getAdapter(): PagedListAdapter<*,*> {
        var feedType = if (arguments == null) {
            "all"
        } else {
            arguments!!.getString("feedType")
        }
        var adapter = FeedAdapter(context,feedType!!)
        return  FeedAdapter(context,feedType)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }




}