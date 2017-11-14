package com.example.kyung.firebasechat.sign;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kyung.firebasechat.Const;
import com.example.kyung.firebasechat.R;
import com.example.kyung.firebasechat.model.User;
import com.example.kyung.firebasechat.util.FormatUtil;
import com.example.kyung.firebasechat.util.DialogUtil;
import com.example.kyung.firebasechat.util.ValidationUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupNextActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private EditText editPasswordRe;
    private EditText editName;
    private Button btnSignUp;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference userRef;

    boolean checkEmail = false;
    boolean checkPassword = false;
    boolean checkPasswordRe = false;
    boolean checkName = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 파이어베이스 모듈 사용 및 데이터베이스 레퍼런스 생성
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(Const.table_user);

        setContentView(R.layout.activity_signup_next);
        initView();

        setEditTextWatcher(editEmail);
        setEditTextWatcher(editName);
        setEditTextWatcher(editPassword);
        setEditTextWatcher(editPasswordRe);

        initButtonSign();
    }

    private void initView() {
        editEmail = findViewById(R.id.editEmail);
        editPassword =findViewById(R.id.editPassword);
        editPasswordRe = findViewById(R.id.editPasswordRe);
        editName = findViewById(R.id.editName);
        btnSignUp = findViewById(R.id.btnSignUp);
    }

    /**
     * 조건 만족시 버튼 활성화
     */
    private void setEnableButton(){
        if(checkEmail && checkName && checkPassword && checkPasswordRe){
            btnSignUp.setEnabled(true);
            int color = ContextCompat.getColor(this, R.color.colorMain);
            btnSignUp.setBackgroundColor(color);
        } else {
            btnSignUp.setEnabled(false);
            int color = ContextCompat.getColor(this, R.color.colorOffFunction);
            btnSignUp.setBackgroundColor(color);
        }
    }

    /**
     * editText에 반응형으로 유효성 체크
     * @param editText
     */
    private void setEditTextWatcher(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (editText.getId()){
                    case R.id.editEmail:
                        checkEmail = ValidationUtil.isValidEmail(s.toString());
                        break;
                    case R.id.editPassword:
                        checkPassword = ValidationUtil.isValidPassword(s.toString());
                        break;
                    case R.id.editPasswordRe:
                        checkPasswordRe = editPassword.getText().toString().equals(s.toString());
                        break;
                    case R.id.editName:
                        checkName = ValidationUtil.isValidName(s.toString());
                        break;
                }
                setEnableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 버튼 클릭시 인증모듈 및 데이터베이스에 추가
     */
    private void initButtonSign(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = editEmail.getText().toString();
                final String password = editPassword.getText().toString();
                final String name = editName.getText().toString();
                final String phone_number = getIntent().getStringExtra(Const.key_phone);
                final String profile_url = "";

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    // 1. 정상등록시 사용자 정보 등록(user 프로필)
                                    FirebaseUser fUser = mAuth.getCurrentUser();
                                    UserProfileChangeRequest.Builder profile = new UserProfileChangeRequest.Builder();
                                    profile.setDisplayName(name);
                                    //profile.setPhotoUri("http:// 사진 uri");
                                    fUser.updateProfile(profile.build());
                                    // 2. 정상등록시 안내 메일 발송
                                    fUser.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    DialogUtil.showDialog(SignupNextActivity.this,getString(R.string.alert_emailMsg),true);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    DialogUtil.showDialog(SignupNextActivity.this,getString(R.string.alert_emailMsgFail),true);
                                                }
                                            });
                                    // 3. 사용자 등록 (firebase에서 불러올 때 "."을 못불러오므로 email을 key로 저장시에는 reaplace 해주어야 한다.)
                                    String keyEmail = FormatUtil.changeMailFormat(email);
                                    User user = new User(fUser.getUid(),name,email,"",phone_number,profile_url);
                                    userRef.child(keyEmail).setValue(user);
                                    // phone과 mail을 연동시킴(나중에 친구찾기할 때 사용)
                                    database.getReference(Const.table_member).child(phone_number).setValue(keyEmail);
                                    setResult(RESULT_OK);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                DialogUtil.showDialog(SignupNextActivity.this,getString(R.string.alert_fail),false);
                            }
                        });
            }
        });
    }
}
