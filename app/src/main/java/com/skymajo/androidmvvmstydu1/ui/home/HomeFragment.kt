package com.skymajo.androidmvvmstydu1.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.skymajo.androidmvvmstydu1.R
import com.skymajo.androidmvvmstydu1.model.Feed
import com.skymajo.androidmvvmstydu1.ui.AbsListFragment
import com.skymajo.libnavannotation.FragmentDestination


@FragmentDestination("home/home",false,true)
class HomeFragment : AbsListFragment<Feed>() {



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
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }



    override fun getAdapter(): PagedListAdapter<Feed, RecyclerView.ViewHolder> {

    }

    override fun OnRefreshListener(linster: OnRefreshListener) {

    }

    override fun OnLoadMoreListener(linster: OnLoadMoreListener) {

    }


}