package com.example.kyung.firebasechat.domain.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Kyung on 2017-11-07.
 */

@IgnoreExtraProperties
public class Room implements Serializable {
    public String id;
    public String title;
    public String last_msg;
    public long last_msg_time;
    public long msg_count;
    public long creation_time;

    @Exclude
    public List<User> member;

}
