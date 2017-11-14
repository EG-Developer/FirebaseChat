package com.example.kyung.firebasechat.model;

/**
 * Created by Kyung on 2017-11-07.
 */

// key는 Room id 이다. (id는 room의 id)
public class Msg {
    public String id;
    public String idx;
    public String msg;
    public String user_id;
    public String name;
    public String time;
    public String type;
    
    public long received_count;
    public long read_count;

    public Msg(){

    }
    public Msg(String idx, String msg, String user_id, String name, String time, String type){
        this.idx = idx;
        this.msg = msg;
        this.user_id = user_id;
        this.name = name;
        this.time = time;
        this.type = type;
    }
}
