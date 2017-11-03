package com.example.kyung.firebasechat.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kyung.firebasechat.R;

/**
 * Created by Kyung on 2017-11-02.
 */

public class StartTab extends FrameLayout {

    ImageView imageView;
    GradientDrawable gd = new GradientDrawable();

    public StartTab(Context context) {
        super(context);
        init();
    }

    private void init(){
        imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.start_tab);
        addView(imageView);
        LayoutParams layoutParams = new  LayoutParams(30, 30);
        setLayoutParams(layoutParams);

    }
    public void clickTab(){
        imageView.setImageResource(R.drawable.start_tab_select);
    }
    public void unClickTab(){
        imageView.setImageResource(R.drawable.start_tab);
    }
}
