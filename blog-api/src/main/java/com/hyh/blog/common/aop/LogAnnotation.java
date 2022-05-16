package com.hyh.blog.common.aop;

import java.lang.annotation.*;

/**
 * @author huyuhui
 * 自定义日志注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";

    String operation() default "";
}
