package com.ivyzh.butterknifedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.ivyzh.mybutterknife_annotations.MyBindView;

public class LoginActivity extends AppCompatActivity {

    @MyBindView(R.id.bt_login)
    public Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
