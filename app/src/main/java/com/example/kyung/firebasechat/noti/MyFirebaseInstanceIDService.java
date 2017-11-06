package com.example.kyung.firebasechat.noti;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Token Refresh
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    // 토큰을 refresh 해준다.
    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }


    private void sendRegistrationToServer(String token) {
        // TODO: 내 데이터베이스의 사용자 token 값을 여기서 갱신한다.
        // ex> 데이터베이스에서 user id 밑에 token 값이 있을 텐데 이를 갱신시키면 된다.
        // String user_node = "user/사용자_id";
        // user_node.setValue(token);
    }
}
