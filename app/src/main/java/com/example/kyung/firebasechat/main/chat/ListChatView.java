package com.example.kyung.firebasechat.main.chat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.kyung.firebasechat.Const;
import com.example.kyung.firebasechat.R;
import com.example.kyung.firebasechat.main.chat.chatdetail.ChatDetailActivity;
import com.example.kyung.firebasechat.model.Room;
import com.example.kyung.firebasechat.util.FormatUtil;
import com.example.kyung.firebasechat.util.PreferenceUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 채팅 목록을 보여주는 ViewPager의 View
 */

public class ListChatView extends FrameLayout implements ListChatAdapter.IMoveDetail {

    FirebaseDatabase database;
    DatabaseReference myRoomRef;
    RecyclerView recyclerView;
    ListChatAdapter adapter = null;
    List<Room> roomList = new ArrayList<>();

    public ListChatView(@NonNull Context context) {
        super(context);
        initView();
        init();
    }

    public void init(){
        database = FirebaseDatabase.getInstance();
        String myKey = FormatUtil.changeMailFormat(PreferenceUtil.getString(getContext(),Const.key_email));
        myRoomRef = database.getReference(Const.table_user).child(myKey).child(Const.table_room);
        setListRoom();
    }

    // 채팅 리스트의 목록을 실시간으로 받도록 설정
    // 따라서 View가 붙어있는 activity에서 listener를 설정하고 제거하고 해주어야 한다.
    public void setListRoom(){
        // adpter가 null 인 경우 리스너를 붙이지 않는다.
        if(adapter != null) {
            myRoomRef.addValueEventListener(roomListValueEventListener);
        }
    }
    public void endListRoomUpdate(){
        myRoomRef.removeEventListener(roomListValueEventListener);
    }
    ValueEventListener roomListValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            roomList.clear();
            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                Room myRoom = snapshot.getValue(Room.class);
                roomList.add(myRoom);
            }
            adapter.setDataAndRefresh(roomList);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void initView(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_chat_list,null);
        recyclerView = view.findViewById(R.id.recyclerChatList);
        recyclerView.addItemDecoration(new ListChatDivider(getContext()));
        adapter = new ListChatAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addView(view);
    }

    @Override
    public void goDetail(String roomId, String roomTitle) {
        Intent intent = new Intent(getContext(), ChatDetailActivity.class);
        intent.putExtra(Const.KEY_ROOM_ID,roomId);
        intent.putExtra(Const.KEY_ROOM_TITLE,roomTitle);
        getContext().startActivity(intent);
    }
}
