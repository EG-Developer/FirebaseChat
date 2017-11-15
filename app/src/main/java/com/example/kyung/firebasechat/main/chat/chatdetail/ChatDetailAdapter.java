package com.example.kyung.firebasechat.main.chat.chatdetail;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kyung.firebasechat.Const;
import com.example.kyung.firebasechat.R;
import com.example.kyung.firebasechat.model.Msg;
import com.example.kyung.firebasechat.util.FormatUtil;
import com.example.kyung.firebasechat.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyung on 2017-11-14.
 */

public class ChatDetailAdapter extends RecyclerView.Adapter<ChatDetailAdapter.Holder> {

    Context context;
    List<Msg> msgList = new ArrayList<>();
    String myEmail;

    public ChatDetailAdapter(Context context){
        this.context = context;
        myEmail = PreferenceUtil.getString(context,Const.key_email);
    }

    public void addDataAndRefresh(Msg msg) {
        msgList.add(msg);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(msgList.get(position).user_email.equals(myEmail))
            return Const.type_me;
        else
            return Const.type_other;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case Const.type_other:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_msg_other, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_msg_me, parent, false);
                break;
        }
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Msg msg = msgList.get(position);
        holder.setUserEmail(msg.user_email);
//        long count = msg.received_count-msg.read_count;
//        holder.setTextCount(count+"");
        holder.setTextMsg(msg.msgText);
        holder.setTextTime(FormatUtil.changeTimeFormat(msg.time,"HH:mm"));

        if(!msg.user_email.equals(myEmail)){
//            holder.setImageProfile();
            holder.setTextMemberName(msg.name);
        }

    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private String user_email;
        private ImageView imageProfile;
        private TextView textMemberName;
        private TextView textMsg;
        private TextView textTime;
        private TextView textCount;
        public Holder(View itemView) {
            super(itemView);

            initView(itemView);
        }

        private void initView(View itemView) {
            imageProfile = itemView.findViewById(R.id.image_profile);
            textMemberName = itemView.findViewById(R.id.textMemberName);
            textMsg = itemView.findViewById(R.id.textMsg);
            textTime = itemView.findViewById(R.id.textTime);
            textCount = itemView.findViewById(R.id.textCount);
        }

        public void setUserEmail(String Email) {
            this.user_email = user_email;
        }
        public void setImageProfile(Uri image) {
            imageProfile.setImageURI(image);
            setProfileClickListner();
        }
        public void setTextMemberName(String name) {
            textMemberName.setText(name);
        }
        public void setTextMsg(String msg) {
            textMsg.setText(msg);
        }
        public void setTextTime(String time) {
            textTime.setText(time);
        }
        public void setTextCount(String count) {
            textCount.setText(count);
        }

        // 프로필 화면 팝업을 띄워줄 수 있는 리스너를 달아준다.
        private void setProfileClickListner(){
            imageProfile.setOnClickListener( view -> {

            });
        }
    }
}
