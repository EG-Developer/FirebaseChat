package com.example.kyung.firebasechat;

/**
 * Created by Kyung on 2017-11-04.
 */

public class Const {
    // request Code
    public static final int REQ_SIGNUP_FIRST = 100; // startActivity의 requestCode
    public static final int PER_CODE = 101; // permission code

    // 불러올 Key 값 정의
    public static final String KEY_ID = "id";
    public static final String key_token = "token";
    public static final String key_password = "password";
    public static final String key_email = "email";
    public static final String key_phone = "phone_number";
    public static final String key_name = "name";
    public static final String key_profile_url = "profile_url";
    public static final String key_auto_sign = "AUTO_SIGN";

    public static final String KEY_ROOM_ID = "room_id";
    public static final String KEY_ROOM_TITLE = "title";
    public static final String KEY_ROOM_CREATIONTIME = "creation_time";
    public static final String KEY_ROOM_LASTMSG = "last_msg";
    public static final String KEY_ROOM_LASTMSG_TIME = "last_msg_time";
    public static final String KEY_ROOM_COUNT = "msg_count";


    // database의 table 명 정의
    public static final String table_user = "user";
    public static final String table_user_friend = "my_friend";
    public static final String table_member = "member";
    public static final String table_room = "room";
    public static final String table_msg = "msg";

    // adapter에 나에대한 int값, 다른사랑들에 대한 int값 정의
    public static final int type_me = 200;
    public static final int type_other = 201;

    // Msg의 type 정의
    public static final String type_image = "image";
    public static final String type_msg = "text";
}
