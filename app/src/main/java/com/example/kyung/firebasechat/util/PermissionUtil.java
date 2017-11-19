package com.example.kyung.firebasechat.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.kyung.firebasechat.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyung on 2017-11-07.
 */

public class PermissionUtil {
    Context context;
    private String[] permissions;

    public PermissionUtil(){ }

    public PermissionUtil(Context context, String[] permissions){
        this.context = context;
        this.permissions = permissions;
    }
    // 승인여부 확인 및 요청
    @TargetApi(Build.VERSION_CODES.M)
    private void customCheckPermission(Activity activity){
        List<String> requires = new ArrayList<>();
        for(String permission : permissions){
            if(activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                requires.add(permission);
            }
        }
        // 승인이 되어 있지 않으면 승인요청, 되어 있으면 init을 callback으로 실행
        if(requires.size()>0){
            activity.requestPermissions(permissions, Const.PER_CODE);
        } else {
            callInit(activity);
        }
    }
    // 버전 체크 후 퍼미션 승인여부 요청
    public void checkVersion(Activity activity){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            customCheckPermission(activity);
        } else {
            callInit(activity);
        }
    }
    // 결과 실행(퍼미션 승인되면 실행)
    public void onResult(Activity activity, int requestCode, int grantResult[]){
        if(requestCode == Const.PER_CODE){
            boolean check = true;
            for(int grant : grantResult){
                if(grant != PackageManager.PERMISSION_GRANTED){
                    check=false;
                    break;
                }
            }
            if(check)
                callInit(activity);
            else
                Toast.makeText(activity,"권한 승인이 필요합니다.",Toast.LENGTH_LONG).show();
                activity.finish();
        }
    }

    public interface CallbackPermission{
        void callInit();
    }

    public static void callInit(Activity activity){
        if (activity instanceof CallbackPermission){
            ((CallbackPermission)activity).callInit();
        } else{
            throw new RuntimeException(activity.toString()
                    + " must implement CallbackPermission");
        }

    }
}
