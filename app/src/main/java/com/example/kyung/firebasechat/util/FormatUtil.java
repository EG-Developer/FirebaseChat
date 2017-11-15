package com.example.kyung.firebasechat.util;

import java.text.SimpleDateFormat;

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

    // long형 시간과 타임 format을 받아 HH:mm 으로 변환
    // time format종류 => yyyy MM dd HH mm ss
    public static String changeTimeFormat(long time, String timeFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        return sdf.format(time);
    }
}
