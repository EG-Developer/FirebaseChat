package com.example.kyung.firebasechat.model;

/**
 * Created by Kyung on 2017-11-07.
 */

public class Msg {
    public String id;
    public String msg;
    public String user_id;
    public String name;
    public String time;
    public long received_count;
    public long read_count;

    public Msg(){

    }

    public Msg(String id, String msg, String user_id, String name, String time){
        this.id = id;
        this.msg = msg;
        this.user_id = user_id;
        this.name = name;
        this.time = time;
    }
}
