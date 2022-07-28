package org.jarvis.spring.aspect.metrix;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jarvis.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

/**
 * https://www.baeldung.com/spring-reading-httpservletrequest-multiple-times
 */
@Aspect
@Component
@Slf4j
public class MonitorAspect {

    @Pointcut("@annotation(Monitor)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = getRequest();
        ServletInputStream inputStream = request.getInputStream();
        IOUtils.consume(inputStream);
        return joinPoint.proceed();
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

    }
}
