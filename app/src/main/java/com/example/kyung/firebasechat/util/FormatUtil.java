package com.example.kyung.firebasechat.util;

/**
 * Created by Kyung on 2017-11-06.
 */

public class FormatUtil {
    // mail의 형태를 변환
    public static String changeMailFormat(String email){
        email = email.replace(" ","");
        return email.replace(".","_");
    }
    // phone의 형태를 변환
    public static String changePhoneFormat(String phone){
        phone = phone.replace(" ","");
        return phone.replace("-","");
    }
}
