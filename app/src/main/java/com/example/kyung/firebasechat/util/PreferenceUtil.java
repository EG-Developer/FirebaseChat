package com.example.kyung.firebasechat.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kyung on 2017-11-06.
 */

public class PreferenceUtil {
    private static final String filename = "FirebaseChatLogin";

    // 1. SharedPerferences 를 생성
    private static SharedPreferences getPreference(Context context){
        return context.getSharedPreferences(filename,Context.MODE_PRIVATE);
    }

    public static void setValue(Context context, String key, String value){
        // 2. 쓰기를 위한 editor 생성
        SharedPreferences.Editor editor = getPreference(context).edit();
        // 3. 키 값 형태로 값을 저장해 둔다.
        editor.putString(key,value);
        editor.commit();

        // 이렇게 하면 안드로이드 앱의 로컬 디렉토리에 아래와 같이 파일이 하나 생성된다.
        /*
            <SharedPreferences>
            <user_id>사용자아이디</user_id>
            </SharedPreferences>
        */
        // editor.remove(key); <- 값을 지우는 방법

    }

    public static void setValue(Context context, String key, long value){
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putLong(key,value);
        editor.commit();
    }
    // 값 가져오기
    public static String getString(Context context, String key){
        return getPreference(context).getString(key,"");
    }

    public static Long getLong(Context context, String key){
        return getPreference(context).getLong(key,0);
    }
}
