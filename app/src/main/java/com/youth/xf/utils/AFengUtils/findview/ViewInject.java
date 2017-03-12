package com.youth.xf.utils.AFengUtils.findview;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by spc on 16-8-10.
 * 需要在onCreate方法中引用   AnnotateUtils.injectViews(this);
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {
    //id就是控件id，在某一个控件上使用注解标注其id
    int value() default -1;
}