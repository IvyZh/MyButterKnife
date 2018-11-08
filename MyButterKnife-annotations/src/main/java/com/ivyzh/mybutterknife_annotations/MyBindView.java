package com.ivyzh.mybutterknife_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Ivy on 2018/11/8.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface MyBindView {
    int value();
}
