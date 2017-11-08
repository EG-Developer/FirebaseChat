package com.example.kyung.firebasechat.main.chat.menu.makeroom;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kyung.firebasechat.R;
import com.example.kyung.firebasechat.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyung on 2017-11-07.
 */

public class MakeRoomAdapter extends RecyclerView.Adapter<MakeRoomAdapter.Holder> {

    List<User> friends = new ArrayList<>();
    Iinvite iinvite;

    public MakeRoomAdapter(Context context){
        if(context instanceof Iinvite){
            iinvite = (Iinvite)context;
        }
    }

    public void setDataAndRefresh(List<User> friends){
        this.friends = friends;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_make_room,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        User friend = friends.get(position);
        holder.setPosition(position);
        holder.setFriendName(friend.name);
        holder.setFriend(friend);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView friendProfile;
        TextView friendName;
        CheckBox friendInvite;
        User friend;
        int position;
        boolean inviteCheck = false;
        public Holder(View itemView) {
            super(itemView);
            initVew(itemView);
            initCheckInvite();
        }
        private void initVew(View view){
            friendProfile = view.findViewById(R.id.friendProfile);
            friendName = view.findViewById(R.id.friendName);
            friendInvite = view.findViewById(R.id.friendInvite);
        }
        private void setFriend(User friend){
            this.friend = friend;
        }
        private void setPosition(int position){
            this.position = position;
        }
        private void setFriendProfile(Uri uri){
            friendProfile.setImageURI(uri);
        }
        private void setFriendName(String name){
            friendName.setText(name);
        }
        private void initCheckInvite(){
            friendInvite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // 체크되면 그 값을 friendlist에 추가시키기 위해 데이터를 변경 (인터페이스 상속)
                    if(isChecked){
                        iinvite.isCheckInviteFriend(true,friend);
                    } else{
                        iinvite.isCheckInviteFriend(false,friend);
                    }
                }
            });
        }
    }
    public interface Iinvite{
        public void isCheckInviteFriend(boolean check, User friend);
    }
}
