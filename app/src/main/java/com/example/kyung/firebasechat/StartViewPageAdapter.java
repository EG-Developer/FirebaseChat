package com.example.kyung.firebasechat;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyung on 2017-11-02.
 */

public class StartViewPageAdapter extends PagerAdapter {
    Context context;

    private final int COUNT = 6;

    public StartViewPageAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return COUNT;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_start_image,null);
        ImageView imageStart = view.findViewById(R.id.imageStart);
        switch (position){
            case 1:imageStart.setImageResource(R.drawable.start_first); break;
            case 2:imageStart.setImageResource(R.drawable.start_second); break;
            case 3:imageStart.setImageResource(R.drawable.start_third); break;
            case 4:imageStart.setImageResource(R.drawable.start_fourth); break;
            case 5:imageStart.setImageResource(R.drawable.start_fifth); break;
            default:
                TextView textView = view.findViewById(R.id.textView);
                textView.setVisibility(View.VISIBLE);
                imageStart.setVisibility(View.INVISIBLE);
                break;
        }
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
