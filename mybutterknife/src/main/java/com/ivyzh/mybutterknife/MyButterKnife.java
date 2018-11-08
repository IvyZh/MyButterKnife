package com.ivyzh.mybutterknife;

import android.app.Activity;

import java.lang.reflect.Constructor;

/**
 * Created by Ivy on 2018/11/8.
 */

public class MyButterKnife {

    public static MyUnbinder bind(Activity activity) {

        try {
            // MainActivity_MyViewBinder
            //ClassNotFoundException: com.ivyzh.butterknifedemo.MainActivity_MyViewBinder
            Class<? extends MyUnbinder> clazz = (Class<? extends MyUnbinder>) Class.forName(activity.getClass().getName() + "_MyViewBinder");

            Constructor<? extends MyUnbinder> constructor = clazz.getDeclaredConstructor(activity.getClass());
            MyUnbinder unbinder = constructor.newInstance(activity);
            return unbinder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MyUnbinder.EMPTY;
    }
}
