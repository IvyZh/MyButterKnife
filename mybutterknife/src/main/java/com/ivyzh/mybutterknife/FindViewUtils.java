package com.ivyzh.mybutterknife;

import android.app.Activity;

/**
 * Created by Ivy on 2018/11/8.
 */

public class FindViewUtils {

    public static <T> T findViewById(Activity activity, int viewId) {
        return (T) activity.findViewById(viewId);
    }
}
