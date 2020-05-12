package com.skymajo.androidmvvmstydu1.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.skymajo.androidmvvmstydu1.utils.PixUtils;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MajoImageView extends AppCompatImageView {
    public MajoImageView(Context context) {
        super(context);
    }

    public MajoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MajoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
//   requireAll = true 代表我们需要将注解里面的方法全写上才会调用此方法
//    @BindingAdapter(value = {"image_url","isCircle"},requireAll = true)
    @BindingAdapter({"image_url","isCircle"})
    public static void setImageUrl(MajoImageView imageView ,String img_url , boolean isCircle){
        RequestBuilder<Drawable> builder = Glide.with(imageView).load(img_url);
        if (isCircle){
            builder.transform(new CircleCrop());
        }
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        if (layoutParams!=null&&layoutParams.width>0&&layoutParams.height>0){
            builder.override(layoutParams.width,layoutParams.height);
        }
        builder.into(imageView);
    }


    public void bindData(int widthPx , int heightPx , int marginLeft ,String imgUrl){
        bindData(widthPx,heightPx,marginLeft,PixUtils.getScreemWidth(),PixUtils.getScreemWidth(),imgUrl);
    }


    public void bindData(int widthPx , int heightPx , int marginLeft ,int maxWidth , int maxHeight ,String imgUrl){
        if(widthPx<=0 || heightPx<=0){
            Glide.with(this).load(imgUrl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    int height = resource.getIntrinsicHeight();
                    int width = resource.getIntrinsicWidth();
                    setSize(width,height,marginLeft,maxWidth,maxHeight);
                    setImageDrawable(resource);
                }
            });
            return;
        }
        setSize(widthPx,heightPx,marginLeft,maxWidth,maxHeight);
        setImageUrl(this,imgUrl,false);
    }

    private void setSize(int width, int height, int marginLeft, int maxWidth, int maxHeight) {
        int finalWidth , finalHeight;
        if(width>height){
            finalWidth = maxWidth;
            finalHeight = (int) (height/(width*1.0f/finalWidth));
        }else{
            finalHeight = maxHeight;
            finalWidth = (int) (width/(height*1.0f/finalHeight));
        }

        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(finalWidth, finalHeight);
        params.leftMargin = height>width? PixUtils.dp2px(marginLeft) :0;
        setLayoutParams(params);

    }


    public void setBlurImageUrl(String coverUrl, int radius) {
        //使用小尺寸图片做高斯模糊，使用override重置尺寸
        Glide.with(this).load(coverUrl).override(50)
                .transform(new BlurTransformation())
                .dontAnimate()
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        setBackground(resource);
                    }
                });
    }
}
