package com.example.kyung.firebasechat.sign;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.kyung.firebasechat.R;

public class SignupActivity extends AppCompatActivity {


    private EditText editSignPhone;
    private Button btnNext;
    private Spinner spinnerCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        initSpinner();
    }

    private void initView() {
        editSignPhone =findViewById(R.id.editSignPhone);
        btnNext = findViewById(R.id.btnNext);
        spinnerCountry = findViewById(R.id.spinnerCountry);
    }

    // 스피너를 세팅
    private void initSpinner(){
        final String data[] = {"대한만국(Republic of Korea)","UnitedStates","日本(Jspan)","中国(China)","HongKong","Indonesia"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
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
}
