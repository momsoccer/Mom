package com.mom.soccer.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mom.soccer.R;
import com.mom.soccer.widget.VeteranToast;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_join_layout);

        VeteranToast.makeToast(getApplicationContext(),"회원가입 페이지입니다", Toast.LENGTH_SHORT).show();
    }
}
