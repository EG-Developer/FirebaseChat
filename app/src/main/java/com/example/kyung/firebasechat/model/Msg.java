package com.example.kyung.firebasechat.model;

/**
 * Created by Kyung on 2017-11-07.
 */

// key는 Room id 이다. (id는 room의 id)
public class Msg {
    public String id;
    public long idx;
    public String msg;
    public String user_email;
    public String name;
    public long time;
    public String type;
    
    public long received_count;
    public long read_count;

    public Msg(){

    }
    public Msg(String id, long idx, String msg, String user_email, String name, long time, String type){
        this.id = id;
        this.idx = idx;
        this.msg = msg;
        this.user_email = user_email;
        this.name = name;
        this.time = time;
        this.type = type;
    }
}
