package com.skymajo.libcommon;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skymajo.libcommon.RoundFrameLayout;
import com.skymajo.libcommon.ViewHelper;

import java.util.List;

public class ShareDialog extends AlertDialog {
    private ShareAdapter shareAdapter;
    private List<ResolveInfo> resolveInfosList;
    private String sShareContent;
    private View.OnClickListener listener;

    public ShareDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);


        RoundFrameLayout frameLayout = new RoundFrameLayout(getContext());
        frameLayout.setViewOutLine(PixUtils.dp2px(20), ViewHelper.RADIUS_TOP);
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        shareAdapter = new ShareAdapter();
        recyclerView.setAdapter(shareAdapter);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = layoutParams.topMargin = layoutParams.rightMargin = layoutParams .bottomMargin;
        layoutParams.gravity = Gravity.CENTER;
        frameLayout.addView(recyclerView,layoutParams);

        setContentView(frameLayout);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        queryShareds();

    }


    public void setShareContent(String str){
        sShareContent = str;
    }

    public void setShareClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    private void queryShareds() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        resolveInfosList = getContext().getPackageManager().queryIntentActivities(intent, 0);
        shareAdapter.notifyDataSetChanged();

    }


    private  class  ShareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


        private final PackageManager packageManager;

        public ShareAdapter(){
            packageManager = getContext().getPackageManager();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_shared_item, parent, false);
            return new RecyclerView.ViewHolder(inflate) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ImageView iv = holder.itemView.findViewById(R.id.iv_img);
            TextView title = holder.itemView.findViewById(R.id.tv_name);
            final ResolveInfo resolveInfo = resolveInfosList.get(position);
            Drawable drawable = resolveInfo.loadIcon(packageManager);
            CharSequence charSequence = resolveInfo.loadLabel(packageManager);
//            for (int i = 0; i < resolveInfosList.size(); i++) {
//                Log.e("ShareDialog","Drawable is null?"+(resolveInfosList.get(i).loadIcon(packageManager) == null)+"");
//                Log.e("ShareDialog","title is null?"+(resolveInfosList.get(i).loadLabel(packageManager) == null)+"");
//                Log.e("ShareDialog","-------------------------------------------------------------------");
//            }
            iv.setImageDrawable(drawable);
            title.setText(charSequence);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转客户端分享
                    String packageName = resolveInfo.activityInfo.packageName;
                    String cls = resolveInfo.activityInfo.name;
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.setComponent(new ComponentName(packageName,cls));
                    intent.putExtra(Intent.EXTRA_TEXT,sShareContent);
                    getContext().startActivity(intent);
                    if (listener!=null){
                        listener.onClick(view);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return resolveInfosList == null?0:resolveInfosList.size();
        }
    }

}
