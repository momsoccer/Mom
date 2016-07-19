package com.mom.soccer.common;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.mom.soccer.R;

/**
 * Created by sungbo on 2016-07-19.
 */
public class CustomProgressDialog extends Dialog {
    public CustomProgressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 지저분한(?) 다이얼 로그 제목을 날림
        setContentView(R.layout.custom_dialog); // 다이얼로그에 박을 레이아웃
    }
}
