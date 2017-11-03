package com.example.kyung.firebasechat;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kyung.firebasechat.customview.StartTab;
import com.example.kyung.firebasechat.sign.SigninActivity;
import com.example.kyung.firebasechat.sign.SignupActivity;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPagerStart;
    TabLayout tabLayoutStart;
    Button btnSignIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initView();
        initTabLayout();
        initViewPager();
        initTabwithPager();
        initButtonListener();
    }

    private void initView(){
        viewPagerStart = findViewById(R.id.viewPagerStart);
        tabLayoutStart = findViewById(R.id.tabLayoutStart);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
    }
    // Tab을 생성
    List<StartTab> startTabList = new ArrayList<>();
    private void initTabLayout(){
        tabLayoutStart.setSelectedTabIndicatorHeight(0);
        for(int i=0 ; i<6 ; i++){
            StartTab startTab = new StartTab(this);
            if(i==0) startTab.clickTab();
            startTabList.add(startTab);
            tabLayoutStart.addTab(tabLayoutStart.newTab().setCustomView(startTab));
        }
    }

    private void initViewPager(){
        StartViewPageAdapter adapter = new StartViewPageAdapter(this);
        viewPagerStart.setAdapter(adapter);
    }

    private void initTabwithPager(){
        viewPagerStart.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutStart));
        tabLayoutStart.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerStart.setCurrentItem(tab.getPosition());
                startTabList.get(tab.getPosition()).clickTab();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                startTabList.get(tab.getPosition()).unClickTab();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initButtonListener(){
        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btnSignIn:
                intent = new Intent(this, SigninActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_scale_in_center, R.anim.anim_scale_out_center);
                finish();
                break;
            default:
                intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                break;
        }
    }
}
