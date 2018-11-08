package com.ivyzh.mybutterknife;

/**
 * Created by Ivy on 2018/11/8.
 */

public interface MyUnbinder {
    void unbind();

    MyUnbinder EMPTY = new MyUnbinder() {
        @Override
        public void unbind() {
        }
    };
}