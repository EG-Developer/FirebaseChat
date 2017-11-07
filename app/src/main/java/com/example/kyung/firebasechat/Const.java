package com.example.kyung.firebasechat;

/**
 * Created by Kyung on 2017-11-04.
 */

public class Const {
    // request Code
    public static final int req_signup_first = 100; // startActivity의 requestCode
    public static final int per_code = 101; // permission code

    // 불러올 Key 값 정의
    public static final String key_id = "id";
    public static final String key_token = "token";
    public static final String key_password = "password";
    public static final String key_email = "email";
    public static final String key_phone = "phone_number";
    public static final String key_name = "name";
    public static final String key_profile_url = "profile_url";
    public static final String key_room_id = "room_id";
    public static final String key_auto_sign = "AUTO_SIGN";

    // database의 table 명 정의
    public static final String table_user = "user";
    public static final String table_user_friend = "my_friend";
    public static final String table_member = "member";
    public static final String table_room = "room";
}
