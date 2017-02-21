package com.zgeorg03.core.controllers.helpers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zgeorg03 on 12/6/16.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {

    boolean required() default false;
    String description() default "";
    String defaultValue() default "";
    String[] extendedDescription() default {};
}
