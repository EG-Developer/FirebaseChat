package com.example.kyung.firebasechat.customview;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.widget.ImageView;

/**
 * Created by Kyung on 2017-11-03.
 */

public class SigninButton extends AppCompatButton {

    ImageView imageView;

    public SigninButton(Context context) {
        super(context);
        imageView = new ImageView(getContext());
    }
}
