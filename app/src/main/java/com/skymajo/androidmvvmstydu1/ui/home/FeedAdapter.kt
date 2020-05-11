package com.skymajo.androidmvvmstydu1.ui.home

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.skymajo.androidmvvmstydu1.model.Feed

class FeedAdapter protected constructor(diffCallback: DiffUtil.ItemCallback<Feed>) :
    PagedListAdapter<Feed, FeedAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return null
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
