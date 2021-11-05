package org.jarvis.spring;


import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * org/springframework/web/servlet/config/annotation/WebMvcConfigurationSupport.java:675 添加自定义ArgumentResolver
 * org/springframework/web/servlet/mvc/method/annotation/RequestMappingHandlerAdapter.java:695 初始化ArgumentResolver
 * https://www.cnblogs.com/mei0619/p/11543709.html ArgumentResolver Demo
 */
public abstract class ArgumentResolverUtils {

    public static final WebMvcConfigurer WEB_MVC_CONFIGURER = new WebMvcConfigurer() {
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.addAll(CUSTOM_RESOLVERS);
        }
    };

    private static final List<HandlerMethodArgumentResolver> CUSTOM_RESOLVERS = new ArrayList<>();

    public static void addHandlerMethodArgumentResolver(HandlerMethodArgumentResolver handlerMethodArgumentResolver) {
        CUSTOM_RESOLVERS.add(handlerMethodArgumentResolver);
    }

}
