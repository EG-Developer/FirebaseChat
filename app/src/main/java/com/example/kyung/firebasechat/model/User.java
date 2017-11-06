package com.example.kyung.firebasechat.model;

/**
 * Created by pc on 11/3/2017.
 */

public class User {
    public String id;
    public String name;
    public String email;
    public String token;
    public String phone_number;

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
