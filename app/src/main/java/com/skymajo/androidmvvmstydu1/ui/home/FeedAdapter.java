package com.skymajo.androidmvvmstydu1.ui.home;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.skymajo.androidmvvmstydu1.R;
import com.skymajo.androidmvvmstydu1.databinding.LayoutFeedTypeImageBinding;
import com.skymajo.androidmvvmstydu1.databinding.LayoutFeedTypeVideoBinding;
import com.skymajo.androidmvvmstydu1.model.Feed;

public class FeedAdapter extends PagedListAdapter<Feed, FeedAdapter.ViewHolder> {

    private  LayoutInflater inflate = null;
    private String category;
    private Context mContext;

    protected FeedAdapter(Context context, String category) {
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
        mContext = context;
        inflate = LayoutInflater.from(context);
        category = category;
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
        View topComment = holder.itemView.findViewById(R.id.fl_top_comment);
        if (getItem(position).getTopComment() == null) {
            topComment.setVisibility(View.GONE);
        } else {
            topComment.setVisibility(View.VISIBLE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public ViewHolder(@NonNull View itemView, ViewDataBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public void bindData(Feed item) {
            //这里之所以手动绑定数据的原因是 图片 和视频区域都是需要计算的
            //而dataBinding的执行默认是延迟一帧的。
            //当列表上下滑动的时候 ，会明显的看到宽高尺寸不对称的问题
            if (binding instanceof LayoutFeedTypeImageBinding){
                LayoutFeedTypeImageBinding imageBindingbinding = (LayoutFeedTypeImageBinding)binding;
                imageBindingbinding.setFeed(item);
                imageBindingbinding.feedImage.bindData(item.getWidth(),item.getHeight(),16,item.getCover());
//                imageBindingbinding.setLifecycleOwner((LifecycleOwner) this);
            }else{
                LayoutFeedTypeVideoBinding videoBinding = (LayoutFeedTypeVideoBinding) binding;
                videoBinding.setFeed(item);
                videoBinding.listPlayerView.bindData(category,item.getWidth(),item.getHeight(),item.getCover(),item.getUrl());
//                videoBinding.setLifecycleOwner((LifecycleOwner) this);
            }
        }
    }
}
