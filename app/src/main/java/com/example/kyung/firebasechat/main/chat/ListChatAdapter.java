package com.example.kyung.firebasechat.main.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kyung.firebasechat.R;
import com.example.kyung.firebasechat.model.Room;
import com.example.kyung.firebasechat.util.FormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyung on 2017-11-02.
 */

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.Holder> {
    IMoveDetail iMoveDetail;
    List<Room> data = new ArrayList<>();

    public ListChatAdapter(FrameLayout frameLayout){
        iMoveDetail = (IMoveDetail)frameLayout;
    }
    public void setDataAndRefresh(List<Room> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_list,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Room room = data.get(position);
        holder.setRoomId(room.id);
//        holder.setImageChatList();
        holder.setTextTitleChatList(room.title);
        holder.setTextMsgDate(room.last_msg_time);

        String msg = "";
        if("".equals(room.last_msg) || room.last_msg == null){
            holder.setTextMsgChatList(msg);
        } else {
            msg = room.last_msg;
            holder.setTextMsgChatList(msg);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private String room_id;
        private String room_title;
        private ImageView imageChatList;
        private TextView textTitleChatList;
        private TextView textMsgChatList;
        private TextView textMsgDate;

        public Holder(View itemView) {
            super(itemView);
            initView(itemView);
            setClickEvent(itemView);
        }

        private void initView(View view){
            imageChatList = view.findViewById(R.id.imageChatLst);
            textTitleChatList = view.findViewById(R.id.textTitleChatList);
            textMsgChatList = view.findViewById(R.id.textMsgChatList);
            textMsgDate = view.findViewById(R.id.textMsgDate);
        }
        private void setClickEvent(View view){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iMoveDetail.goDetail(room_id, room_title);
                }
            });
        }
        public void setRoomId(String room_id){
            this.room_id = room_id;
        }
        public void setImageChatList(Uri uri){

        }
        public void setTextTitleChatList(String title){
            room_title = title;
            textTitleChatList.setText(title);
            textTitleChatList.setPaintFlags(textTitleChatList.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        }
        public void setTextMsgChatList(String msg){
            textMsgChatList.setText(msg);
        }
        public void setTextMsgDate(long date){
            if(date == 0) textMsgDate.setText("");
            else textMsgDate.setText(FormatUtil.changeTimeFormat(date,"yyyy-MM-dd"));
        }
    }

    public interface IMoveDetail{
        public void goDetail(String roomId, String roomTitle);
    }
}
