package com.example.kyung.firebasechat.main.chat.menu.makeroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kyung.firebasechat.Const;
import com.example.kyung.firebasechat.R;
import com.example.kyung.firebasechat.main.chat.chatdetail.ChatDetailActivity;
import com.example.kyung.firebasechat.model.Room;
import com.example.kyung.firebasechat.model.User;
import com.example.kyung.firebasechat.util.FormatUtil;
import com.example.kyung.firebasechat.util.DialogUtil;
import com.example.kyung.firebasechat.util.PreferenceUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeRoomActivity extends AppCompatActivity implements MakeRoomAdapter.Iinvite{

    private Toolbar toolbar;
    private EditText searchFriend;
    private RecyclerView recyclerMakeRoom;
    private Button btnChat;
    private MakeRoomAdapter adapter;

    private User me;
    private Room room;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference userRef;
    private DatabaseReference roomRef;

    private List<User> inviteFriendList = new ArrayList<>();
    List<User> my_friend = new ArrayList<>();
    List<User> show_friendList = new ArrayList<>();
    List<User> invited_friend = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_room);

        String email = PreferenceUtil.getString(this,Const.key_email);

        // 로그인된 uer에 대한 정보를 가져옴
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Const.table_user).child(FormatUtil.changeMailFormat(email));
        userRef = database.getReference(Const.table_user);
        roomRef = database.getReference(Const.table_room);
        me = new User();

        inviteFriendList.clear();
        invited_friend.clear();

        // room을 Serialize로 받아온다.
        room = (Room) getIntent().getSerializableExtra(Const.table_room);
        if(room == null){
            room = new Room();
            room.id = roomRef.push().getKey();
        } else{
        }
        loadInvitedFriend();
        Log.e("찍힘",room.id);

        initView();
        initRecyclerView();
    }

    private void loadInvitedFriend(){
        roomRef.child(room.id).child(Const.table_member).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    invited_friend.add(user);
                }
                loadFriendList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        searchFriend = findViewById(R.id.searchFriend);
        recyclerMakeRoom = findViewById(R.id.recyclerMakeRoom);
        btnChat = findViewById(R.id.btnChat);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle("친구선택");
        return true;
    }

    private void setSearchFriendListener(){
        searchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                show_friendList.clear();
                if("".equals(s.toString()) || s.toString() == null){
                    adapter.setDataAndRefresh(my_friend);
                } else {
                    for (User user : my_friend) {
                        if (user.name.contains(s)) {
                            show_friendList.add(user);
                        }
                    }
                    adapter.setDataAndRefresh(show_friendList);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initRecyclerView(){
        adapter = new MakeRoomAdapter(this);
        recyclerMakeRoom.setAdapter(adapter);
        recyclerMakeRoom.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadFriendList(){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 자신의 데이터를 세팅
                my_friend.clear();
                Map map = (HashMap) dataSnapshot.getValue();
                me.id = (String) map.get(Const.key_id);
                me.email = (String) map.get(Const.key_email);
                me.token = (String) map.get(Const.key_token);
                me.phone_number = (String) map.get(Const.key_phone);
                me.name = (String) map.get(Const.key_name);
                me.profile_url = (String) map.get(Const.key_profile_url);
                // 친구들 데이터를 세팅
                if(dataSnapshot.hasChild(Const.table_user_friend)){
                    DataSnapshot friendMember = dataSnapshot.child(Const.table_user_friend);
                    for(DataSnapshot snapshot : friendMember.getChildren()){
                        User user = snapshot.getValue(User.class);
                        my_friend.add(user);
                        for(User invitedFriend : invited_friend){
                            if(user.id.equals(invitedFriend.id)){
                                my_friend.remove(user);
                                break;
                            }
                        }
                    }
                }
                adapter.setDataAndRefresh(my_friend);
                setSearchFriendListener();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("loadFriendError",databaseError.toString());
            }
        });
    }

    /**
     * 버튼 클릭시 room을 생성하는 메소드
     * @param view
     */
    public void makeRoom(View view){
        inviteFriendList.add(me);
        // 1명 이상 초대시 방 생성
        if(inviteFriendList.size()>1){
            // room_title이 존재하지 않을 경우 title 만들기
            if(room.title == null) {
                room.title = "";
                for (User user : inviteFriendList) {
                    room.title += "," + user.name;
                    if (room.title.length() > 15) {
                        room.title = room.title.substring(0, 15) + "...";
                        break;
                    }
                }
                room.title = room.title.substring(1);
                roomRef.child(room.id).setValue(room);
            }
            // 초대된 member를 room에 저장
            for(User member : inviteFriendList){
                String emailKey = FormatUtil.changeMailFormat(member.email);
                roomRef.child(room.id).child(Const.table_member).child(emailKey).setValue(member);
                userRef.child(emailKey).child(Const.table_room).child(room.id).setValue(room);
            }

            // 만든 룸으로 이동
            Intent intent = new Intent(this, ChatDetailActivity.class);
            intent.putExtra(Const.key_room_id,room.id);
            startActivity(intent);
            finish();
        } else{
            DialogUtil.showDialog(this,"1명 이상의 친구를 초대해 주세요",false);
            inviteFriendList.clear();
        }
    }

    @Override
    public void isCheckInviteFriend(boolean check, User friend) {
        if(check){
            inviteFriendList.add(friend);
        } else{
            inviteFriendList.remove(friend);
        }
    }
}
