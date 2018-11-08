package com.ivyzh.butterknifedemo;

/**
 * Created by Ivy on 2018/11/8.
 */

public class MainActivity_ViewBinder {
    MainActivity target;

    public MainActivity_ViewBinder(MainActivity target) {
        this.target = target;
        target.username = target.findViewById(R.id.user);
    }
}
