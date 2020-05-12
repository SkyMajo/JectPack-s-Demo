package com.skymajo.androidmvvmstydu1.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.skymajo.androidmvvmstydu1.databinding.LayoutFeedTypeImageBinding;
import com.skymajo.androidmvvmstydu1.databinding.LayoutFeedTypeVideoBinding;
import com.skymajo.androidmvvmstydu1.model.Feed;

public class FeedAdapter extends PagedListAdapter<Feed, FeedAdapter.ViewHolder> {

    private final LayoutInflater inflate = null;
    private String category;

    protected FeedAdapter(@NonNull DiffUtil.ItemCallback<Feed> diffCallback, String category) {
        super(new DiffUtil.ItemCallback<Feed>() {
            @Override
            public boolean areItemsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return false;
            }
        });
        this.category = category;
    }

    @Override
    public int getItemViewType(int position) {
        Feed feed = getItem(position);
        return feed.getItemType();
    }


    public static final int  TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = null;
        if (viewType == TYPE_IMAGE ) {
            binding = LayoutFeedTypeImageBinding.inflate(this.inflate);
        }else{
            binding= LayoutFeedTypeVideoBinding.inflate(this.inflate);
        }

        return new ViewHolder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bindData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public ViewHolder(@NonNull View itemView, ViewDataBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public void bindData(Feed item) {
            if (binding instanceof LayoutFeedTypeImageBinding){
                LayoutFeedTypeImageBinding imageBindingbinding = (LayoutFeedTypeImageBinding)binding;
                imageBindingbinding.setFeed(item);
                imageBindingbinding.feedImage.bindData(item.getWidth(),item.getHeight(),16,item.getCover());
            }else{
                LayoutFeedTypeVideoBinding videoBinding = (LayoutFeedTypeVideoBinding) binding;
                videoBinding.setFeed(item);
                videoBinding.listPlayerView.bindData(category,item.getWidth(),item.getHeight(),item.getCover(),item.getUrl());
            }
        }
    }
}
