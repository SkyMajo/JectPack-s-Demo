package com.skymajo.androidmvvmstydu1.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

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

}
