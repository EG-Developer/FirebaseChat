package com.example.kyung.firebasechat.main.chat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.kyung.firebasechat.R;

/**
 * Created by Kyung on 2017-11-20.
 */

public class ListChatDivider extends RecyclerView.ItemDecoration {

    public Drawable mDivider;
    Context context;

    public ListChatDivider(Context context){
        this.context = context;
        mDivider = context.getResources().getDrawable(R.drawable.item_divider);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() -1 ){
            outRect.bottom = (int)context.getResources().getDimension(R.dimen.chat_list_space);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + (int)context.getResources().getDimension(R.dimen.chat_list_image_height);
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for(int i=0 ; i<childCount ; i++){
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top,right,bottom);
            mDivider.draw(c);
        }
    }
}
