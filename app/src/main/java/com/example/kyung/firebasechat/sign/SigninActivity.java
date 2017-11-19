package com.example.kyung.firebasechat.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kyung.firebasechat.Const;
import com.example.kyung.firebasechat.FirstActivity;
import com.example.kyung.firebasechat.R;
import com.example.kyung.firebasechat.main.MainActivity;
import com.example.kyung.firebasechat.util.FormatUtil;
import com.example.kyung.firebasechat.util.DialogUtil;
import com.example.kyung.firebasechat.util.PreferenceUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class SigninActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference userRef;
    private EditText editEmail;
    private EditText editPassword;
    private Button btnSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // 인증 모듈 연결
        mAuth = FirebaseAuth.getInstance();
        // 데이터베이스 user 레퍼런스 생성
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(Const.table_user);

        if(PreferenceUtil.getString(this,Const.key_auto_sign).equals("true")){
            String email = PreferenceUtil.getString(this,Const.key_email);
            String password = PreferenceUtil.getString(this,Const.key_password);
            signin(email,password);
        } else {
            initView();
            setBtnSignin();
        }
    }

    private void initView() {
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnSignin = findViewById(R.id.btnSignin);
    }

    /**
     * 버튼을 세팅하여 로그인 처리 시작
     */
    private void setBtnSignin(){
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editEmail.getText().toString();
                final String password = editPassword.getText().toString();

                signin(email,password);
            }
        });
    }

    /**
     * email과 password를 받아 firebase에 로그인 처리 요청
     * @param email
     * @param password
     */
    private void signin(final String email, final String password){
        Log.e("email","========================="+email);
        Log.e("password","==================="+password);
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser fUser = mAuth.getCurrentUser();
                            if(fUser.isEmailVerified()) {
                                // preference에 값을 저장
                                PreferenceUtil.setValue(getBaseContext(), Const.KEY_ID, fUser.getUid());
                                PreferenceUtil.setValue(getBaseContext(), Const.key_email, email);
                                PreferenceUtil.setValue(getBaseContext(), Const.key_password, password);
                                PreferenceUtil.setValue(getBaseContext(), Const.key_auto_sign,"true");

                                String refreshToken = FirebaseInstanceId.getInstance().getToken();
                                userRef.child(FormatUtil.changeMailFormat(email)).child(Const.key_token).setValue(refreshToken);

                                // 로그인 진행
                                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                DialogUtil.showDialog(SigninActivity.this,getString(R.string.alert_checkEmail),false);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ㅇㅇㅇ",e.getMessage());
                        DialogUtil.showDialog(SigninActivity.this,getString(R.string.alert_differentauth),false);
                    }
                });
    }

    /**
     * 엑티비티를 취소?로 종료시 애니메이션 처리로 activity를 불러옴
     * auto_sign =true 이면 그냥 finish,
     * 아니면 처음 화면 불러옴
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(PreferenceUtil.getString(this,Const.key_auto_sign).equals("true")){
            finish();
        } else {
            Intent intent = new Intent(this, FirstActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_scale_in_center, R.anim.anim_scale_out_center);
            finish();
        }
    }
}
