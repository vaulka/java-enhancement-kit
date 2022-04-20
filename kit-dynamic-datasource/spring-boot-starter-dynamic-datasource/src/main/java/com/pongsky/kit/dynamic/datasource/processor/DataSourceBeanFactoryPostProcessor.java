package com.pongsky.kit.dynamic.datasource.processor;

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
    private static final String DATA_SOURCE_INITIALIZER_POST_PROCESSOR = "dataSourceInitializerPostProcessor";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (registry.containsBeanDefinition(DATA_SOURCE_INITIALIZER_POST_PROCESSOR)) {
            // 将数据源初始化后置执行器进行删除
            registry.removeBeanDefinition(DATA_SOURCE_INITIALIZER_POST_PROCESSOR);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

}
