package com.example.kyung.firebasechat.main.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kyung.firebasechat.R;
import com.example.kyung.firebasechat.model.Room;

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
        String msg = "";
        if("".equals(room.last_msg) || room.last_msg == null){
            holder.setTextMsgChatList(msg);
        } else {
            if (room.last_msg.length() > 15) {
                msg = room.last_msg.substring(0, 15) + "...";
            }
            holder.setTextMsgChatList(msg);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private String room_id;
        private ImageView imageChatList;
        private TextView textTitleChatList;
        private TextView textMsgChatList;

        public Holder(View itemView) {
            super(itemView);
            initView(itemView);
            setClickEvent(itemView);
        }

        private void initView(View view){
            imageChatList = view.findViewById(R.id.imageChatLst);
            textTitleChatList = view.findViewById(R.id.textTitleChatList);
            textMsgChatList = view.findViewById(R.id.textMsgChatList);
        }
        private void setClickEvent(View view){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iMoveDetail.goDetail(room_id);
                }
            });
        }

        public void setRoomId(String room_id){
            this.room_id = room_id;
        }
        public void setImageChatList(Uri uri){

        }
        public void setTextTitleChatList(String title){
            textTitleChatList.setText(title);
        }
        public void setTextMsgChatList(String msg){
            textMsgChatList.setText(msg);
        }
    }

    public interface IMoveDetail{
        public void goDetail(String roomId);
    }
}
