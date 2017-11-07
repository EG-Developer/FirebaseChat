package com.example.kyung.firebasechat.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.kyung.firebasechat.R;
import com.example.kyung.firebasechat.main.chat.menu.makeroom.MakeRoomActivity;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabMain;
    private ViewPager viewPagerMain;
    int toolbarNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initViewPager();
        initTablayout();
        setViewWithTab();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tabMain = findViewById(R.id.tabMain);
        viewPagerMain = findViewById(R.id.viewPagerMain);
        // 툴바를 액션바처럼 쓰기 위해 호출해준다.
        setSupportActionBar(toolbar);
    }

    private void initViewPager(){
        MainPagerAdapter adapter = new MainPagerAdapter(this);
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
}
