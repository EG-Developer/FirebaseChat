package com.example.kyung.firebasechat.main.chat.menu.makeroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.example.kyung.firebasechat.Const;
import com.example.kyung.firebasechat.R;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MakeRoomActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText searchFriend;
    private RecyclerView recyclerMakeRoom;
    private Button btnChat;
    private MakeRoomAdapter adapter;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private DatabaseReference memberRef;
    private DatabaseReference roomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_room);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(Const.table_user);
        memberRef = database.getReference(Const.table_member);
        roomRef = database.getReference(Const.table_room);

        initView();
        initRecyclerView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        searchFriend = findViewById(R.id.searchFriend);
        recyclerMakeRoom = findViewById(R.id.recyclerMakeRoom);
        btnChat = findViewById(R.id.btnChat);
        setSupportActionBar(toolbar);
    }

    private void initRecyclerView(){
        adapter = new MakeRoomAdapter();
        recyclerMakeRoom.setAdapter(adapter);
        recyclerMakeRoom.setLayoutManager(new LinearLayoutManager(this));
    }
}
