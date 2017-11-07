package com.example.kyung.firebasechat.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyung on 2017-11-07.
 */

public class ContactUtil {

    public static List<String> phoneNumLoad(Context context){
        List<String> data = new ArrayList<>();
        // 1. content Resolver
        ContentResolver resolver = context.getContentResolver();
        // 2. 데이터 uri 정의
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        // 3. 가져올 컬럼 정의
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        // 4. 쿼리 결과를 Cursor에 순서대로
        Cursor cursor = resolver.query(uri,projection,null,null,null);
        // 5. cursor 반복 처리
        if(cursor != null) {
            while (cursor.moveToNext()) {
                int index = cursor.getColumnIndex(projection[0]);
                data.add(cursor.getString(index));
            }
        }
        return data;
    }
}
