package org.jarvis.spring.aspect.retry;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Retryable {

    /**
     * retry times
     */
    int value() default 3;

    /**
     * 重试时间间隔,unit: MILLISECOND
     */
    int interval() default 0;

}
