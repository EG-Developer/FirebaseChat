package com.example.kyung.firebasechat.model;

import java.util.List;

/**
 * Created by pc on 11/3/2017.
 */

public class User {
    public String id;
    public String name;
    public String email;
    public String token;
    public String phone_number;

    public List<User> my_firend;
    public List<Room> chatting_room;

    public User(){
        // default for firebase
    }

    public User(String id, String name, String email, String token, String phone_number){
        this.id = id;
        this.name = name;
        this.email = email;
        this.token = token;
        this.phone_number = phone_number;
    }

}
