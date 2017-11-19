package com.example.kyung.firebasechat.main.chat.chatdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private User me;
    private List<String> roomMemberKey = new ArrayList<>();
    private Map<Long, Msg> msgAllList = new HashMap<>();

    private FirebaseDatabase database;
    private DatabaseReference myRoomRef;
    private DatabaseReference userRef;
    private DatabaseReference msgRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        init();
    }

    private void init(){
        String roomId = getIntent().getStringExtra(Const.KEY_ROOM_ID);
        String myRoomTitle = getIntent().getStringExtra(Const.KEY_ROOM_TITLE);
        roomMemberKey.clear();
        msgAllList.clear();

        database = FirebaseDatabase.getInstance();
        myRoomRef = database.getReference(Const.table_room).child(roomId);
        msgRef = database.getReference(Const.table_msg).child(roomId);
        userRef = database.getReference(Const.table_user);

        initView(myRoomTitle);
        setRecyclerChatAdapter();
        setSendMsg();
    }

    private void setMsgListener(){
        msgRef.addValueEventListener(msgValueEventListener);
    }
    private void removeMsgListener(){
        msgRef.removeEventListener(msgValueEventListener);
    }
    ValueEventListener msgValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot == null) return;
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

    private void setMyRoomListener(){
        myRoomRef.addValueEventListener(roomValueEventListener);
    }
    private void removeMyRoomListener(){
        myRoomRef.removeEventListener(roomValueEventListener);
    }
    ValueEventListener roomValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            room = new Room();
            Map roomMap = (HashMap) dataSnapshot.getValue();
            room.id = (String) roomMap.get(Const.KEY_ID);
            room.title = (String) roomMap.get(Const.KEY_ROOM_TITLE);
            room.creation_time = (long) roomMap.get(Const.KEY_ROOM_CREATIONTIME);

            String myKey = FormatUtil.changeMailFormat(PreferenceUtil.getString(ChatDetailActivity.this,Const.key_email));
            DataSnapshot snapshotMember = dataSnapshot.child(Const.table_member);

            roomMemberKey.clear();
            for(DataSnapshot snapshot : snapshotMember.getChildren()){
                User user = snapshot.getValue(User.class);
                if(snapshot.getKey().equals(myKey)){
                    me=user;
                }
                roomMemberKey.add(snapshot.getKey());
            }
            if(me == null){
                Toast.makeText(ChatDetailActivity.this, "방 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(ChatDetailActivity.this, "불러오기 실패", Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    private void initView(String myRoomTitle) {
        toolbarChat = findViewById(R.id.toolbarChat);
        recyclerChat =  findViewById(R.id.recyclerChat);
        btnType = findViewById(R.id.btnType);
        btnEmoticon =  findViewById(R.id.btnEmoticon);
        editMsg = findViewById(R.id.editMsg);
        btnSend =  findViewById(R.id.btnSend);
        setSupportActionBar(toolbarChat);
        if("".equals(myRoomTitle) || myRoomTitle == null){
            getSupportActionBar().setTitle("Fail");
        } else {
            getSupportActionBar().setTitle(myRoomTitle);
        }

    }
    private void setRecyclerChatAdapter(){
        adapter = new ChatDetailAdapter(this);
        recyclerChat.setAdapter(adapter);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setSendMsg(){
        btnSend.setOnClickListener(view ->{
            if(me == null || room == null) return;
            String msgText = editMsg.getText().toString();
            if(!"".equals(msgText) && msgText != null){
                String id = room.id;
                long idx = adapter.getItemCount()+1;
                String user_email = me.email;
                String name = me.name;
                long time = System.currentTimeMillis();
                String type = Const.type_msg;
                Msg msg = new Msg(id, idx, msgText, user_email, name, time, type);
                msgRef.child(idx+"").setValue(msg);

                // room 정보를 수정
                addMsgInfoToRoom(msgText, time, idx);

                editMsg.setText("");
            }
        });
    }

    private void addMsgInfoToRoom(String msgText, long msgTime, long count){
        myRoomRef.child(Const.KEY_ROOM_LASTMSG).setValue(msgText);
        myRoomRef.child(Const.KEY_ROOM_LASTMSG_TIME).setValue(msgTime);
        myRoomRef.child(Const.KEY_ROOM_COUNT).setValue(count);

        for(String key : roomMemberKey){
            userRef.child(key).child(Const.table_room).child(room.id).child(Const.KEY_ROOM_LASTMSG).setValue(msgText);
            userRef.child(key).child(Const.table_room).child(room.id).child(Const.KEY_ROOM_LASTMSG_TIME).setValue(msgTime);
            userRef.child(key).child(Const.table_room).child(room.id).child(Const.KEY_ROOM_COUNT).setValue(count);
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

    /**
     * 엑티비티가 Resume, Pause 할때 Listener를 부착 및 해제
     */
    @Override
    protected void onResume() {
        super.onResume();
        setMyRoomListener();
        setMsgListener();
    }
    @Override
    protected void onPause() {
        super.onPause();
        removeMyRoomListener();
        removeMsgListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        msgAllList.clear();
    }
}
