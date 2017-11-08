package com.example.kyung.firebasechat.main.chat.menu.makeroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kyung.firebasechat.Const;
import com.example.kyung.firebasechat.R;
import com.example.kyung.firebasechat.main.chat.chatdetail.ChatDetailActivity;
import com.example.kyung.firebasechat.model.Room;
import com.example.kyung.firebasechat.model.User;
import com.example.kyung.firebasechat.util.ChangeUtil;
import com.example.kyung.firebasechat.util.DialogUtil;
import com.example.kyung.firebasechat.util.PreferenceUtil;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference userRef;
    private DatabaseReference roomRef;

    private List<User> friendList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_room);

        String email = PreferenceUtil.getString(this,Const.key_email);

        // 로그인된 uer에 대한 정보를 가져옴
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Const.table_user).child(ChangeUtil.changeMailFormat(email));
        userRef = database.getReference(Const.table_user);
        roomRef = database.getReference(Const.table_room);
        me = new User();
        friendList.clear();

        initView();
        initRecyclerView();
        loadFriendList();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        searchFriend = findViewById(R.id.searchFriend);
        recyclerMakeRoom = findViewById(R.id.recyclerMakeRoom);
        btnChat = findViewById(R.id.btnChat);
        setSupportActionBar(toolbar);
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
                List<User> my_friend = new ArrayList<>();
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
                    }
                }
                adapter.setDataAndRefresh(my_friend);
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
        friendList.add(me);
        // 1명 이상 초대시 방 생성
        if(friendList.size()>1){
            // DB에 room 추가
            String roomId = roomRef.push().getKey();
            // title 만들기
            String title = "";
            for(User user : friendList){
                title += "," + user.name;
                if(title.length()>12){
                    title = title.substring(0,12)+"...";
                    break;
                }
            }
            title = title.substring(1);
            // 변수에 저장
            Room room = new Room();
            room.id = roomId;
            room.title = title;
            // firebase database에 저장
            roomRef.child(roomId).setValue(room);
            for(User member : friendList){
                String emailKey = ChangeUtil.changeMailFormat(member.email);
                roomRef.child(roomId).child(Const.table_member).child(emailKey).setValue(member);
                userRef.child(emailKey).child(Const.table_room).child(room.id).setValue(room);
            }
            /*
                룸 이동 전 각각의 멤버에도 Room 추가 필요함
             */


            // 만든 룸으로 이동
            Intent intent = new Intent(this, ChatDetailActivity.class);
            intent.putExtra(Const.key_room_id,roomId);
            startActivity(intent);
            finish();
        } else{
            DialogUtil.showDialog(this,"1명 이상의 친구를 초대해 주세요",false);
            friendList.clear();
        }
    }

    @Override
    public void isCheckInviteFriend(boolean check, User friend) {
        if(check){
            friendList.add(friend);
        } else{
            friendList.remove(friend);
        }
        Log.e("friend 추가","========================="+friend.name+" // "+check + " // "+ friendList.size());
    }
}
