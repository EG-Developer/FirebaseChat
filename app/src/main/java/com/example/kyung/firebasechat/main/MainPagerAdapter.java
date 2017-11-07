package com.example.kyung.firebasechat.main;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.kyung.firebasechat.main.board.BoardView;
import com.example.kyung.firebasechat.main.call.CallView;
import com.example.kyung.firebasechat.main.chat.ListChatView;
import com.example.kyung.firebasechat.main.friend.ListFriendView;
import com.example.kyung.firebasechat.main.myinfo.MyInfoView;

import java.util.ArrayList;
import java.util.List;

/**
 * main page들을 연결해주는 pagerAdapter
 */

public class MainPagerAdapter extends PagerAdapter {
    Context context;
    public static final int COUNT = 5;

    List<View> viewList = new ArrayList<>();

    public MainPagerAdapter(Context context){
        this.context = context;
        View view = new ListFriendView(context); viewList.add(view);
        view = new ListChatView(context); viewList.add(view);
        view = new BoardView(context); viewList.add(view);
        view = new CallView(context); viewList.add(view);
        view = new MyInfoView(context); viewList.add(view);
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = viewList.get(position);
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
