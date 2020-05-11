package com.skymajo.androidmvvmstydu1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.skymajo.androidmvvmstydu1.databinding.LayoutRefrshViewBinding
import com.skymajo.androidmvvmstydu1.view.EmptyView

abstract class AbsListFragment<T> :Fragment(){
    private lateinit var adapter: PagedListAdapter<T, RecyclerView.ViewHolder>
    var binding: LayoutRefrshViewBinding? = null
    var mRecyclerView: RecyclerView? = null
    var mRefrshLayout :RefreshLayout? = null
    var mEmptyView :EmptyView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutRefrshViewBinding.inflate(inflater, container, false)
        mRecyclerView = binding!!.recyclerView
        mRefrshLayout = binding!!.refrshLayout
        mEmptyView = binding!!.emptyView

        mRefrshLayout!!.setEnableRefresh(true)
        mRefrshLayout!!.setEnableLoadMore(true)
        mRefrshLayout!!.setOnRefreshListener(OnRefreshListener())
        mRefrshLayout!!.setOnLoadMoreListener(OnLoadMoreListener())

        adapter = getAdapter()
        mRecyclerView!!.setAdapter(adapter)
        mRecyclerView!!.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        mRecyclerView!!.itemAnimator = null



        return binding!!.root
    }

    fun finishRefrsh(hasData:Boolean){
        var currentList = adapter.currentList
        var hasDatas = hasData || currentList != null && currentList.size > 0
        var state = mRefrshLayout!!.state
        if(state.isFooter && state.isOpening){
            mRefrshLayout!!.finishLoadMore()
        }else if(state.isHeader && state.isOpening){
            mRefrshLayout!!.finishRefresh()
        }

        if(hasDatas){
            mEmptyView!!.visibility = View.GONE
        }else{
            mEmptyView!!.visibility = View.VISIBLE
        }

    }

    fun submitList(pagedList:PagedList<T>){
        if(pagedList.size>0){
            adapter.submitList(pagedList)
        }
        finishRefrsh(pagedList.size>0)
    }

    abstract fun getAdapter (): PagedListAdapter<T, RecyclerView.ViewHolder>

    abstract fun OnRefreshListener():OnRefreshListener
    abstract fun OnLoadMoreListener():OnLoadMoreListener

}