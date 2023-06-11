package com.example.commonutils.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    //操作模块
    String modul() default "默认操作模块";

    //操作类型
    String type() default "默认操作类型";

    //操作说明
    String desc() default "默认操作说明";

}
