package com.skymajo.androidmvvmstydu1.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.skymajo.androidmvvmstydu1.AbsViewModel
import com.skymajo.androidmvvmstydu1.R
import com.skymajo.androidmvvmstydu1.databinding.LayoutRefrshViewBinding
import com.skymajo.androidmvvmstydu1.ui.home.FeedAdapter
import com.skymajo.androidmvvmstydu1.view.EmptyView
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class AbsListFragment<T, M : AbsViewModel<T>> :Fragment(), OnRefreshListener, OnLoadMoreListener{
    private lateinit var adapter: PagedListAdapter<T, RecyclerView.ViewHolder>
    var binding: LayoutRefrshViewBinding? = null
    var mRecyclerView: RecyclerView? = null
    var mRefrshLayout :RefreshLayout? = null
    var mEmptyView :EmptyView? = null
    private lateinit var viewModel: M


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutRefrshViewBinding.inflate(inflater, container, false)
        mRecyclerView = binding!!.recyclerView
        mRefrshLayout = binding!!.refreshLayout
        mEmptyView = binding!!.emptyView

        mRefrshLayout!!.setEnableRefresh(true)
        mRefrshLayout!!.setEnableLoadMore(true)
        mRefrshLayout!!.setOnRefreshListener(this)
        mRefrshLayout!!.setOnLoadMoreListener(this)

        adapter = getAdapter() as PagedListAdapter<T, RecyclerView.ViewHolder>
        mRecyclerView!!.setAdapter(adapter)
        mRecyclerView!!.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        var dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!,R.drawable.list_divider)!!)
        mRecyclerView!!.addItemDecoration(dividerItemDecoration)
        mRecyclerView!!.itemAnimator = null


        afterCreateView()
        return binding!!.root
    }

    abstract fun afterCreateView()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var type : ParameterizedType = javaClass.genericSuperclass as ParameterizedType
        var actualTypeArguments : Array<Type>  = type.actualTypeArguments
        if (actualTypeArguments.size>1){
            var argument:Type = actualTypeArguments!![1] as Type
            var modelClz = argument.javaClass.asSubclass(AbsViewModel::class.java)
            viewModel = ViewModelProviders.of(this).get(modelClz) as M
            viewModel.data.observe(this,
                Observer<PagedList<T>> { t -> adapter.submitList(t) })
            viewModel.mutableLiveData.observe(this,
                Observer<Boolean>{ t-> finishRefrsh(t)})
        }
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

    abstract fun getAdapter ():  PagedListAdapter<*,*>


}