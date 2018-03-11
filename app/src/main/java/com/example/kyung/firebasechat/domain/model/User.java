package com.example.kyung.firebasechat.domain.model;

import com.google.firebase.database.Exclude;

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
    public String profile_url;

    @Exclude
    public List<User> my_friend;
    @Exclude
    public List<Room> chatting_room;

    public User(){
        // default for firebase
    }

    public User(String id, String name, String email, String token, String phone_number, String profile_url){
        this.id = id;
        this.name = name;
        this.email = email;
        this.token = token;
        this.phone_number = phone_number;
        this.profile_url = profile_url;
    }

}
