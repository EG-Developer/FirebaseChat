package com.example.kyung.firebasechat.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kyung on 2017-11-04.
 */

public class ValidationUtil {
    // verification : 절차(과정)의 정상적으로 진행되었는지를 검사
    // validation   : 요구사항에 맞도록 정상적으로 동작하는지 확인
    //                값을 검사할 때는 validation 이 맞을거 같음....

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("user");

    /**
     * 패스워드 검증하기
     * @param password
     * @return
     */
    public static boolean isValidPassword(String password) {
        String regex = "^[A-Za-z0-9]{8,16}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 이메일 검증하기
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        String regex = "^[_a-z0-9A-Z-]+(.[_a-z0-9A-Z-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 이름 검증하기
     * @param str
     * @return
     */
    public static boolean isValidName(String str){
        String regex = "^[가-힣A-Za-z0-9]{2,12}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 폰 번호 검증하기 (중복해서 저장되어 있는지도 확인함)
     * @param phone
     * @return
     */
    public static boolean isValidPhone(String phone){
        if(phone.length()>3 && !phone.substring(0,3).equals("010")){
            return false;
        }
        String regex = "^[0-9]{11,11}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        return m.matches();
    }
}
