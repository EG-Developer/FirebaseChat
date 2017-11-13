package com.example.kyung.firebasechat.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.kyung.firebasechat.Const;
import com.example.kyung.firebasechat.R;
import com.example.kyung.firebasechat.main.board.BoardView;
import com.example.kyung.firebasechat.main.call.CallView;
import com.example.kyung.firebasechat.main.chat.ListChatView;
import com.example.kyung.firebasechat.main.chat.menu.makeroom.MakeRoomActivity;
import com.example.kyung.firebasechat.main.friend.ListFriendView;
import com.example.kyung.firebasechat.main.myinfo.MyInfoView;
import com.example.kyung.firebasechat.model.User;
import com.example.kyung.firebasechat.util.ChangeUtil;
import com.example.kyung.firebasechat.util.ContactUtil;
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

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabMain;
    private ViewPager viewPagerMain;
    int toolbarNumber = 0;

    FirebaseDatabase database;
    DatabaseReference userRef;
    DatabaseReference memberRef;

    View viewFriend, viewChat, viewBoard, viewCall, viewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(Const.table_user);
        memberRef = database.getReference(Const.table_member);

        initView();
        initViewPager();
        initTablayout();
        setViewWithTab();

        setFriendList();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tabMain = findViewById(R.id.tabMain);
        viewPagerMain = findViewById(R.id.viewPagerMain);
        // 툴바를 액션바처럼 쓰기 위해 호출해준다.
        setSupportActionBar(toolbar);
    }

    private void initViewPager(){
        List<View> viewList = new ArrayList<>();
        viewFriend = new ListFriendView(this); viewList.add(viewFriend);
        viewChat = new ListChatView(this); viewList.add(viewChat);
        viewBoard = new BoardView(this); viewList.add(viewBoard);
        viewCall = new CallView(this); viewList.add(viewCall);
        viewInfo = new MyInfoView(this); viewList.add(viewInfo);
        MainPagerAdapter adapter = new MainPagerAdapter(this, viewList);
        viewPagerMain.setAdapter(adapter);
    }

    private void initTablayout(){
        tabMain.addTab(tabMain.newTab().setIcon(R.drawable.icon_friend));
        tabMain.addTab(tabMain.newTab().setIcon(R.drawable.icon_message));
        tabMain.addTab(tabMain.newTab().setIcon(android.R.drawable.ic_menu_recent_history));
        tabMain.addTab(tabMain.newTab().setIcon(android.R.drawable.ic_menu_call));
        tabMain.addTab(tabMain.newTab().setIcon(android.R.drawable.ic_menu_view));
        tabMain.setSelectedTabIndicatorColor(ContextCompat.getColor(this,R.color.colorWhite));
    }

    /**
     * tab의 변화에 따라 viewPager 및 toolbar를 변경
     */
    private void setViewWithTab(){
        viewPagerMain.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabMain));
        tabMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerMain.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()){
                    // tab 변화마다 toolbar의 menu를 변경 및 다시 그리도록 함
                    case 0: toolbarNumber = 0; invalidateOptionsMenu(); break;
                    case 1: toolbarNumber = 1; invalidateOptionsMenu(); break;
                    case 2: toolbarNumber = 2; invalidateOptionsMenu(); break;
                    case 3: toolbarNumber = 3; invalidateOptionsMenu(); break;
                    case 4: toolbarNumber = 4; invalidateOptionsMenu(); break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * 툴바를 Tab에 따라 세팅한다.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (toolbarNumber){
            case 1: getMenuInflater().inflate(R.menu.icon_menu_chat,menu); setTitle("대화"); break;
            case 2: getMenuInflater().inflate(R.menu.icon_menu_friend,menu); setTitle("타임라인");break;
            case 3: getMenuInflater().inflate(R.menu.icon_menu_chat,menu); setTitle("통화");break;
            case 4: getMenuInflater().inflate(R.menu.icon_menu_friend,menu); setTitle("더보기");break;
            default: getMenuInflater().inflate(R.menu.icon_menu_friend,menu); setTitle("친구"); break;
        }
        return true;
    }

    /**
     * toobar의 menu에 따라 클릭효과를 다르게 준다.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_friend: break;
            case R.id.menu_search: break;
            case R.id.menu_editFriend: break;
            case R.id.menu_friendSetting: break;
            case R.id.menu_add_room:
                Intent intent = new Intent(MainActivity.this, MakeRoomActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_editMsg: break;
            case R.id.menu_rearrangeRoom: break;
        }
        return true;
    }

    /**
     * 저장된 전화번호부에 있는 친구목록을 불러와 firebase에 업데이트
     */
    public void setFriendList(){
        final List<String> phoneList = ContactUtil.phoneNumLoad(this);
        final String myKey = ChangeUtil.changeMailFormat(PreferenceUtil.getString(this,Const.key_email));
        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // phone을 통해 mail의 key를 가져옴
                final List<String> mailKey = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for(String phoneNum : phoneList){
                        if(phoneNum.equals(snapshot.getKey())){
                            String mail = (String) snapshot.getValue();
                            mailKey.add(ChangeUtil.changeMailFormat(mail));
                            break;
                        }
                    }
                }

                // 받은 friend_key들을 통해 내 친구로 등록
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(String mail : mailKey){
                            User friend = new User();
                            Map map = (HashMap) dataSnapshot.child(mail).getValue();
                            friend.id = (String) map.get(Const.key_id);
                            friend.email = (String) map.get(Const.key_email);
                            friend.token = (String) map.get(Const.key_token);
                            friend.phone_number = (String) map.get(Const.key_phone);
                            friend.name = (String) map.get(Const.key_name);
                            friend.profile_url = (String) map.get(Const.key_profile_url);
                            userRef.child(myKey).child(Const.table_user_friend).child(mail).setValue(friend);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // Activity의 상태에 따른 리스너들 설정
    @Override
    protected void onResume() {
        super.onResume();
        ((ListChatView)viewChat).setListRoom();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((ListChatView)viewChat).endListRoomUpdate();
    }
}
