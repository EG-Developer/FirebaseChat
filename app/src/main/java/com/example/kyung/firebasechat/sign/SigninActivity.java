package com.example.kyung.firebasechat.sign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kyung.firebasechat.FirstActivity;
import com.example.kyung.firebasechat.R;

public class SigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_scale_in_center, R.anim.anim_scale_out_center);
        finish();
    }
}
