package com.skymajo.androidmvvmstydu1.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.skymajo.androidmvvmstydu1.R;

public class EmptyView extends LinearLayout {
    private ImageView emptyIcon;
    private TextView emptyText;
    private MaterialButton emptyAction;
    public EmptyView(@NonNull Context context) {
        this(context,null);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_empty_view, this, true);
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);

        emptyIcon = findViewById(R.id.empty_icon);
        emptyText = findViewById(R.id.empty_text);
        emptyAction = findViewById(R.id.empty_action);

    }

    public void setEmptyIcon(@DrawableRes int iconRes){
        emptyIcon.setImageResource(iconRes);
    }

    public void setEmptyText(String text){
//        emptyText.setText(TextUtils.isEmpty(text)?"暂无数据":text);
    }


    public void setButtonAction(String text,View.OnClickListener listener){
        emptyAction.setText(TextUtils.isEmpty(text)?"好的":text);
        emptyAction.setOnClickListener(listener);
    }



}
