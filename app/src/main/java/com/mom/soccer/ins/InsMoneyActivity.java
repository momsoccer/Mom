package com.mom.soccer.ins;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mom.soccer.R;
import com.mom.soccer.widget.VeteranToast;

public class InsMoneyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ins_money);

        VeteranToast.makeToast(getApplicationContext(),"베타 테스트 기간에는 포인트를 설정 하실 수 없습니다.", Toast.LENGTH_SHORT).show();
    }
}
