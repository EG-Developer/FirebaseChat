package com.example.kyung.firebasechat.main.chat.chatdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kyung.firebasechat.Const;
import com.example.kyung.firebasechat.R;
import com.example.kyung.firebasechat.main.chat.menu.makeroom.MakeRoomActivity;
import com.example.kyung.firebasechat.model.Room;
import com.example.kyung.firebasechat.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatDetailActivity extends AppCompatActivity {

    private Toolbar toolbarChat;
    private RecyclerView recyclerChat;
    private ChatDetailAdapter adapter;
    private Button btnType;
    private Button btnEmoticon;
    private EditText editMsg;
    private Button btnSend;

    private Room room = null;
    private String room_id;
    private List<User> roomMember = new ArrayList<>();

    private FirebaseDatabase database;
    private DatabaseReference myRoomRef;
    private DatabaseReference msgRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        room_id = getIntent().getStringExtra(Const.key_room_id);
        roomMember.clear();

        database = FirebaseDatabase.getInstance();
        myRoomRef = database.getReference(Const.table_room).child(room_id);
        msgRef = database.getReference(Const.table_msg).child(room_id);
        loadRoomInfo();

    }

    private void loadRoomInfo(){
        myRoomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                room = new Room();
                Map map = (HashMap) dataSnapshot.getValue();
                room.id = room_id;
                room.title = (String) map.get(Const.key_room_title);

                DataSnapshot snapshotMember = dataSnapshot.child(Const.table_member);
                for(DataSnapshot snapshot : snapshotMember.getChildren()){
                    User user = snapshot.getValue(User.class);
                    roomMember.add(user);
                }
                // 모든 작업이 끝난 후 view를 init
                initView();
                setMsgListener();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatDetailActivity.this, "채팅방 불러오기 실패", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setMsgListener(){
        if(room != null) msgRef.addValueEventListener(msgValueEventListener);
    }
    private void removeMsgListener(){
        msgRef.removeEventListener(msgValueEventListener);
    }
    ValueEventListener msgValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                // 여기서 adapter에 세팅
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void initView() {
        toolbarChat = findViewById(R.id.toolbarChat);
        recyclerChat =  findViewById(R.id.recyclerChat);
        btnType = findViewById(R.id.btnType);
        btnEmoticon =  findViewById(R.id.btnEmoticon);
        editMsg = findViewById(R.id.editMsg);
        btnSend =  findViewById(R.id.btnSend);
        setSupportActionBar(toolbarChat);
        if("".equals(room.title) || room.title == null){
            getSupportActionBar().setTitle("Fail");
        } else {
            getSupportActionBar().setTitle(room.title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.icon_menu_chatdetail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_call:
                // 전화걸기
                break;
            case R.id.menu_invite:
                Intent intent = new Intent(this, MakeRoomActivity.class);
                intent.putExtra(Const.table_room, room);
                startActivity(intent);
                break;
            case R.id.menu_outRoom:
                // 방에서 나가기
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMsgListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeMsgListener();
    }
}
