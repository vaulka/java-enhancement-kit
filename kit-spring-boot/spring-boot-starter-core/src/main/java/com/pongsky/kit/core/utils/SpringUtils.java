package com.pongsky.kit.core.utils;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring 工具类
 *
 * @author pengsenhao
 */
public class SpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware {

    /**
     * Bean 工厂
     */
    private static ConfigurableListableBeanFactory beanFactory = null;

    /**
     * 应用上下文
     */
    private static ApplicationContext applicationContext = null;

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    /**
     * 获取 Bean 工厂
     *
     * @return 获取 Bean 工厂
     */
    public static ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * 获取应用上下文
     *
     * @return 获取应用上下文
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
