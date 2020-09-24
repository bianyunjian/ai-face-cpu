package com.hankutech.ai.face.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 获取SpringBean对象
 *
 * 普通对象可以通过它来获取SpringBean
 *
 * @author ZhangXi
 */
@Component
@Lazy(false)
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext thisApplicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.thisApplicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return thisApplicationContext;
    }

    public static Object getBean(String name) {
        return thisApplicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> tClass) {
        return thisApplicationContext.getBean(tClass);
    }
}
