package com.kexue.common.log.annotation;

import java.lang.annotation.*;

/**
 * 自定义日志注解
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * @return 接口描述
     */
    String info() default "";
}
