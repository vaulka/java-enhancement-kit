package com.pongsky.kit.dynamic.datasource.autoconfigure;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import com.pongsky.kit.dynamic.datasource.config.DruidConfig;
import com.pongsky.kit.dynamic.datasource.core.DynamicDataSource;
import com.pongsky.kit.dynamic.datasource.core.DynamicDataSourceContextHolder;
import com.pongsky.kit.dynamic.datasource.processor.DataSourceBeanFactoryPostProcessor;
import com.pongsky.kit.dynamic.datasource.properties.MultiDataSourceProperties;
import com.pongsky.kit.dynamic.datasource.web.aspect.around.DataSourceAspect;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 动态数据源自动装配
 *
 * @author pengsenhao
 **/
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({MultiDataSourceProperties.class})
@Import({DataSourceBeanFactoryPostProcessor.class, DruidConfig.class, DataSourceAspect.class})
public class DynamicDataSourceAutoConfiguration {

    private final ApplicationContext applicationContext;

    /**
     * 动态数据源配置
     *
     * @param multiDataSourceProperties 多数据源配置
     * @return 动态数据源
     */
    @Bean
    @Primary
    public DynamicDataSource dynamicDataSource(MultiDataSourceProperties multiDataSourceProperties) {
        String defaultDataSourceName = DynamicDataSourceContextHolder.DEFAULT_DATA_SOURCE_NAME;
        Map<Object, Object> targetDataSources = new ConcurrentHashMap<>(16);
        // 配置默认数据源
        DruidDataSourceWrapper defaultDataSource = this.registerDataSource(defaultDataSourceName);
        targetDataSources.put(defaultDataSourceName, defaultDataSource);
        // 校验配置信息
        this.validationMultiDataSourceProperties(multiDataSourceProperties, defaultDataSourceName, defaultDataSource);
        if (multiDataSourceProperties.getMultiDatasets() != null && multiDataSourceProperties.getMultiDatasets().size() > 0) {
            // 配置额外数据源
            // 连接池参数复用 druid 连接池参数，数据库连接配置信息未填写则复用默认配置信息
            for (Map.Entry<String, MultiDataSourceProperties.DataSourceProperties> entry :
                    multiDataSourceProperties.getMultiDatasets().entrySet()) {
                String dataSourceName = entry.getKey();
                MultiDataSourceProperties.DataSourceProperties properties = entry.getValue();
                DruidDataSourceWrapper dataSource = this.registerDataSource(dataSourceName);
                dataSource.setUrl(properties.getUrl());
                if (StringUtils.isNotBlank(properties.getDriverClassName())) {
                    dataSource.setDriverClassName(properties.getDriverClassName());
                }
                if (StringUtils.isNotBlank(properties.getUsername())) {
                    dataSource.setUsername(properties.getUsername());
                }
                if (StringUtils.isNotBlank(properties.getPassword())) {
                    dataSource.setPassword(properties.getPassword());
                }
                targetDataSources.put(dataSourceName, dataSource);
            }
        }
        DynamicDataSourceContextHolder.setDataSourceNames(targetDataSources.keySet().stream()
                .map(Object::toString)
                .collect(Collectors.toList()));
        return new DynamicDataSource(defaultDataSource, targetDataSources);
    }

    /**
     * 校验多数据源配置信息
     *
     * @param multiDataSourceProperties 多数据源配置信息
     * @param defaultDataSourceName     默认数据源名称
     * @param defaultDataSource         默认数据源
     */
    private void validationMultiDataSourceProperties(MultiDataSourceProperties multiDataSourceProperties,
                                                     String defaultDataSourceName,
                                                     DruidDataSourceWrapper defaultDataSource) {
        if (multiDataSourceProperties.getMultiDatasets() != null && multiDataSourceProperties.getMultiDatasets().size() > 0) {
            if (multiDataSourceProperties.getMultiDatasets().containsKey(defaultDataSourceName)) {
                throw new IllegalArgumentException(MessageFormat.format("[{0}] 为默认数据源名称，禁止重名",
                        defaultDataSourceName));
            }
            for (Map.Entry<String, MultiDataSourceProperties.DataSourceProperties> entry :
                    multiDataSourceProperties.getMultiDatasets().entrySet()) {
                String dataSourceName = entry.getKey();
                MultiDataSourceProperties.DataSourceProperties properties = entry.getValue();
                Assert.notNull(properties.getUrl(), MessageFormat.format(
                        "yml 配置 spring.multi-datasets[{0}].url 不能为空", dataSourceName));
            }
            List<String> duplicateUrls = multiDataSourceProperties.getMultiDatasets().entrySet().stream()
                    .collect(Collectors.groupingBy(e -> e.getValue().getUrl(), Collectors.counting()))
                    .entrySet().stream()
                    .filter(e -> e.getValue() > 1)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            if (duplicateUrls.size() > 0) {
                throw new IllegalArgumentException(MessageFormat.format("动态数据源 url {0} 出现重复",
                        duplicateUrls));
            }
            List<String> urls = multiDataSourceProperties.getMultiDatasets().values().stream()
                    .map(MultiDataSourceProperties.DataSourceProperties::getUrl)
                    .distinct()
                    .collect(Collectors.toList());
            if (urls.contains(defaultDataSource.getUrl())) {
                throw new IllegalArgumentException(
                        MessageFormat.format("动态数据源 url 与默认数据源 url [{0}] 出现重复",
                                defaultDataSource.getUrl()));
            }
        }
    }

    /**
     * 注册并获取数据源
     *
     * @param dataSourceName 数据源名称
     * @return 注册并获取数据源
     */
    private DruidDataSourceWrapper registerDataSource(String dataSourceName) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)
                ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DruidDataSourceWrapper.class);
        beanFactory.registerBeanDefinition(dataSourceName, beanDefinition);
        return (DruidDataSourceWrapper) beanFactory.getBean(dataSourceName);
    }

}
