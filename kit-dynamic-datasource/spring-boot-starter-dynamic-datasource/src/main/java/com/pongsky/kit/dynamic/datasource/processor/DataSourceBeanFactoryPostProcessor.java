package com.pongsky.kit.dynamic.datasource.processor;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * 解决多数据源导致的循环依赖问题
 *
 * @author pengsenhao
 **/
public class DataSourceBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    /**
     * 数据源初始化后置执行器
     */
    private static final String BEAN_NAME = "dataSourceInitializerPostProcessor";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (registry.containsBeanDefinition(BEAN_NAME)) {
            registry.removeBeanDefinition(BEAN_NAME);
        }
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

}
