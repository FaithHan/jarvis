package org.jarvis.spring.aspect.retry;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author hanfei08@baidu.com
 * @since 2021/11/15 10:29
 */

@Aspect
@Component
@Slf4j
public class RetryAspect {

    @Pointcut("@annotation(Retryable)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Retryable retryable = method.getAnnotation(Retryable.class);
        int retryTimes = retryable.value();
        int interval = retryable.interval();
        int retryCount = 0;
        while (true) {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                if (++retryCount < retryTimes) {
                    TimeUnit.MILLISECONDS.sleep(interval);
                    log.info(method + " retry " + retryCount + " times");
                } else {
                    throw e;
                }
            }
        }
    }
}
