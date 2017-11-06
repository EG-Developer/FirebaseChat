package com.example.kyung.firebasechat.util;

/**
 * Created by Kyung on 2017-11-06.
 */

public class ChangeUtil {
    // mail의 형태를 변환
    public static String changeMailFormat(String email){
        return email.replace(".","_");
    }
}
