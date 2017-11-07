package com.example.kyung.firebasechat.util;

import android.content.Context;

import com.example.kyung.firebasechat.Const;
import com.example.kyung.firebasechat.model.Room;
import com.example.kyung.firebasechat.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 파이어베이스 유틸... 일단 보류
 */

public class FirebaseDataUtil {
//    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//    private static CallbackFriendList callbackFriendList;
//
//    public static void loadfriend(String mailKey, Context context){
//        DatabaseReference userRef = database.getReference(Const.table_user).child(mailKey);
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.hasChild(Const.table_user_friend)){
//                    callbackFriendList.callFriend()
//                } else{
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    public interface CallbackFriendList{
//        public List<User> callFriend(List<User> my_friend);
//    }
}
