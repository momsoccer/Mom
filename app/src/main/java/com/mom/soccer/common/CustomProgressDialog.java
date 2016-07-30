package com.mom.soccer.common;

import android.app.Dialog;
import android.content.Context;

import com.mom.soccer.R;

/**
 * Created by sungbo on 2016-07-19.
 */
public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context) {
        super(context);
        setContentView(R.layout.custom_dialog); // 다이얼로그에 박을 레이아웃
    }
}
