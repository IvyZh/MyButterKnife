package com.ivyzh.butterknifedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ivyzh.mybutterknife.MyButterKnife;
import com.ivyzh.mybutterknife.MyUnbinder;
import com.ivyzh.mybutterknife_annotations.MyBindView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MainActivity extends AppCompatActivity {

    // 使用MainActivity_ViewBinder
    TextView username;
    // 使用ButterKnife
    @BindView(R.id.user2)
    TextView username2;
    // 使用自己写的MyBindView注解
    @MyBindView(R.id.user3)
    TextView username3;
    @MyBindView(R.id.user4)
    TextView tvUserName4;

    Unbinder mUnbinder;
    MyUnbinder mMyUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        new MainActivity_ViewBinder(this);
        mMyUnbinder = MyButterKnife.bind(this);
        username.setText("use MainActivity_ViewBinder");
        username2.setText("use butterknife.");
        username3.setText("use MyBindView");
        tvUserName4.setText("use by MyBindView");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mMyUnbinder.unbind();
    }
}
