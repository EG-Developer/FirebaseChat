package com.example.kyung.firebasechat.main.chat.chatdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import com.example.kyung.firebasechat.model.Msg;
import com.example.kyung.firebasechat.model.Room;
import com.example.kyung.firebasechat.model.User;
import com.example.kyung.firebasechat.util.FormatUtil;
import com.example.kyung.firebasechat.util.PreferenceUtil;
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
    private User me;
    private String myKey;
    private List<User> roomMember = new ArrayList<>();
    private Map<Long, Msg> msgAllList = new HashMap<>();

    private FirebaseDatabase database;
    private DatabaseReference myRoomRef;
    private DatabaseReference msgRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        room_id = getIntent().getStringExtra(Const.key_room_id);
        myKey = FormatUtil.changeMailFormat(PreferenceUtil.getString(this,Const.key_email));
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
                msgAllList.clear();
                room.id = room_id;
                room.title = (String) map.get(Const.key_room_title);

                DataSnapshot snapshotMember = dataSnapshot.child(Const.table_member);
                for(DataSnapshot snapshot : snapshotMember.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(FormatUtil.changeMailFormat(user.email).equals(myKey)){
                        me=user;
                    }
                    roomMember.add(user);
                }
                if(me == null){
                    Toast.makeText(ChatDetailActivity.this, "방 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }

                // 모든 작업이 끝난 후 view 및 listener 를 init
                initView();
                setRecyclerChatAdapter();
                setMsgListener();
                setSendMsg();
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
                Msg msg = snapshot.getValue(Msg.class);

                if(msgAllList.containsKey(msg.idx)) continue;
                else {
                    msgAllList.put(msg.idx, msg);
                    adapter.addDataAndRefresh(msg);
                    recyclerChat.scrollToPosition(adapter.getItemCount() - 1);
                }
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
    private void setRecyclerChatAdapter(){
        adapter = new ChatDetailAdapter(this);
        recyclerChat.setAdapter(adapter);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setSendMsg(){
        btnSend.setOnClickListener(view ->{
            String msgText = editMsg.getText().toString();
            if(!"".equals(msgText) && msgText != null){
                String id = room_id;
                long idx = adapter.getItemCount()+1;
                String user_email = me.email;
                String name = me.name;
                long time = System.currentTimeMillis();
                String type = Const.type_msg;
                Msg msg = new Msg(id, idx, msgText, user_email, name, time, type);
                msgRef.child(idx+"").setValue(msg);

                editMsg.setText("");
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        msgAllList.clear();
    }
}
