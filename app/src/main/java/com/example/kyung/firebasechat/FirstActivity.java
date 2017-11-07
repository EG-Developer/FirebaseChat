package com.example.kyung.firebasechat;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.kyung.firebasechat.customview.StartTab;
import com.example.kyung.firebasechat.sign.SigninActivity;
import com.example.kyung.firebasechat.sign.SignupActivity;
import com.example.kyung.firebasechat.sign.SignupNextActivity;
import com.example.kyung.firebasechat.util.PermissionUtil;
import com.example.kyung.firebasechat.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener, PermissionUtil.CallbackPermission {

    ViewPager viewPagerStart;
    TabLayout tabLayoutStart;
    Button btnSignIn, btnSignUp;

    PermissionUtil permissionUtil;

    private static String[] permissions = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 퍼미션 체크
        permissionUtil = new PermissionUtil(this,permissions);
        permissionUtil.checkVersion(this);
    }

    // 퍼미션 체크 후에 실행
    private void init(){
        // 로그인되어 있는지 체크
        if(PreferenceUtil.getString(this,Const.key_auto_sign).equals("true")){
            Intent intent = new Intent(this, SigninActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_first);
            initView();
            initTabLayout();
            initViewPager();
            initTabwithPager();
            initButtonListener();
        }
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
                break;
            default:
                intent = new Intent(this, SignupActivity.class);
                overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_in_bottom);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtil.onResult(this,Const.per_code,grantResults);
    }

    @Override
    public void callInit() {
        init();
    }
}
