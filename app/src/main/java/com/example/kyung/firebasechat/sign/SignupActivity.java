package com.example.kyung.firebasechat.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.kyung.firebasechat.Const;
import com.example.kyung.firebasechat.R;
import com.example.kyung.firebasechat.util.ValidationUtil;
import com.google.android.gms.common.SignInButton;

public class SignupActivity extends AppCompatActivity {


    private EditText editSignPhone;
    private EditText editPutAuth;
    private Button btnNext;
    private Spinner spinnerCountry;

    boolean checkPhoneNumber = false;
    boolean checkAuthNumber = false;
    private SignInButton btnGoogleSign;
    private Button btnFacebookSign;
    private InputMethodManager imm;
    private ConstraintLayout constraint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isAcceptingText()){

        }

        initView();
        initSpinner();
        setEditPutAuth();
        setEditSignPhone();
        setBtnNext();
    }

    private void initView() {
        editSignPhone = findViewById(R.id.editSignPhone);
        btnNext = findViewById(R.id.btnNext);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        editPutAuth = findViewById(R.id.editPutAuth);
        btnGoogleSign = findViewById(R.id.btnGoogleSign);
        btnFacebookSign = findViewById(R.id.btnFacebookSign);
        constraint = findViewById(R.id.constraint);
    }

    // 스피너를 세팅
    private void initSpinner() {
        final String data[] = {"대한만국(Republic of Korea)", "UnitedStates", "日本(Jspan)", "中国(China)", "HongKong", "Indonesia"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // 버튼 리스너 달기
    private void setBtnNext() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editSignPhone.getText().toString().replace("-", "");
                Intent intent = new Intent(SignupActivity.this, SignupNextActivity.class);
                intent.putExtra(Const.key_phone, phone);
                startActivity(intent);
            }
        });
    }

    // 조건을 만족하면 버튼의 색상 변경 및 enable 되게 함
    private void enableNext() {
        if (checkPhoneNumber && checkAuthNumber) {
            btnNext.setEnabled(true);
            int color = ContextCompat.getColor(this, R.color.colorMain);
            btnNext.setBackgroundColor(color);
        } else {
            btnNext.setEnabled(false);
            int color = ContextCompat.getColor(this, R.color.colorOffFunction);
            btnNext.setBackgroundColor(color);
        }
    }

    // 핸드폰 번호를 반응형으로 버튼을 체크 (TextWatcher 사용) 및 자동 - 추가
    private boolean isFormatting;
    private boolean deletingHyphen;
    private int hyphenStart;
    private boolean deletingBackward;

    private void setEditSignPhone() {


        // 반응형으로 만들기
        editSignPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (isFormatting)
                    return;

                // Make sure user is deleting one char, without a selection
                final int selStart = Selection.getSelectionStart(s);
                final int selEnd = Selection.getSelectionEnd(s);
                if (s.length() > 1 // Can delete another character
                        && count == 1 // Deleting only one character
                        && after == 0 // Deleting
                        && s.charAt(start) == '-' // a hyphen
                        && selStart == selEnd) { // no selection
                    deletingHyphen = true;
                    hyphenStart = start;
                    // Check if the user is deleting forward or backward
                    if (selStart == start + 1) {
                        deletingBackward = true;
                    } else {
                        deletingBackward = false;
                    }
                } else {
                    deletingHyphen = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputPhone = s.toString().replace("-", "");
                checkPhoneNumber = ValidationUtil.isValidPhone(inputPhone);
                enableNext();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormatting)
                    return;

                isFormatting = true;

                // If deleting hyphen, also delete character before or after it
                if (deletingHyphen && hyphenStart > 0) {
                    if (deletingBackward) {
                        if (hyphenStart - 1 < s.length()) {
                            s.delete(hyphenStart - 1, hyphenStart);
                        }
                    } else if (hyphenStart < s.length()) {
                        s.delete(hyphenStart, hyphenStart + 1);
                    }
                }
                if (s.length() == 3 || s.length() == 8) {
                    s.append('-');
                }

                isFormatting = false;
            }
        });
    }

    // 인증번호 확인(반응형)
    private void setEditPutAuth() {
        editPutAuth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAuthNumber = "1111".equals(s.toString());
                enableNext();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
