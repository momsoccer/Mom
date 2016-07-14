package com.mom.soccer.login;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mom.soccer.R;
import com.tsengvn.typekit.TypekitContextWrapper;

public class JoinAndLoginActivity extends AppCompatActivity {



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_and_login);
    }
}
