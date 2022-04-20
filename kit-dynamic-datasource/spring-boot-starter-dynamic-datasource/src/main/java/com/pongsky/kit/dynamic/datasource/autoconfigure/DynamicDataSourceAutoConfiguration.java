package com.pongsky.kit.dynamic.datasource.autoconfigure;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceWrapper;
import com.alibaba.druid.util.Utils;
import com.pongsky.kit.dynamic.datasource.core.DynamicDataSource;
import com.pongsky.kit.dynamic.datasource.core.DynamicDataSourceContextHolder;
import com.pongsky.kit.dynamic.datasource.processor.DataSourceBeanFactoryPostProcessor;
import com.pongsky.kit.dynamic.datasource.properties.MultiDataSourceProperties;
import com.pongsky.kit.dynamic.datasource.web.aspect.around.DataSourceAspect;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.util.Assert;

import javax.servlet.Filter;
import java.io.IOException;
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
@Import({DataSourceBeanFactoryPostProcessor.class, DataSourceAspect.class})
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
        Map<Object, Object> targetDataSources = new ConcurrentHashMap<>(16);
        // 配置默认数据源
        DruidDataSourceWrapper defaultDataSource = this.registerDataSource(DynamicDataSourceContextHolder.DEFAULT_DATA_SOURCE_NAME);
        targetDataSources.put(DynamicDataSourceContextHolder.DEFAULT_DATA_SOURCE_NAME, defaultDataSource);
        // 校验配置信息
        if (multiDataSourceProperties.getMultiDatasets() != null && multiDataSourceProperties.getMultiDatasets().size() > 0) {
            List<String> dataSourceNames = multiDataSourceProperties.getMultiDatasets().keySet().stream()
                    .distinct()
                    .collect(Collectors.toList());
            if (dataSourceNames.size() != multiDataSourceProperties.getMultiDatasets().size()) {
                throw new IllegalArgumentException("动态数据源名称出现重复");
            }
            if (dataSourceNames.contains(DynamicDataSourceContextHolder.DEFAULT_DATA_SOURCE_NAME)) {
                throw new IllegalArgumentException(MessageFormat.format("{0} 为默认数据源名称，禁止重名",
                        DynamicDataSourceContextHolder.DEFAULT_DATA_SOURCE_NAME));
            }
            for (Map.Entry<String, MultiDataSourceProperties.DataSourceProperties> entry :
                    multiDataSourceProperties.getMultiDatasets().entrySet()) {
                String dataSourceName = entry.getKey();
                MultiDataSourceProperties.DataSourceProperties properties = entry.getValue();
                Assert.notNull(properties.getUrl(), MessageFormat.format(
                        "yml 配置 spring.multi-datasets[{0}].url 不能为空", dataSourceName));
            }
            List<String> urls = multiDataSourceProperties.getMultiDatasets().values().stream()
                    .map(MultiDataSourceProperties.DataSourceProperties::getUrl)
                    .distinct()
                    .collect(Collectors.toList());
            if (urls.size() != multiDataSourceProperties.getMultiDatasets().values().size()) {
                throw new IllegalArgumentException("动态数据源 url 出现重复");
            }
            if (urls.contains(defaultDataSource.getUrl())) {
                throw new IllegalArgumentException("动态数据源 url 与默认数据源 url 出现重复");
            }
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

    /**
     * 去除 druid 监控底部广告
     *
     * @return 拦截器
     * @throws IOException IOException
     * @author pengsenhao
     */
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.druid.stat-view-servlet.enabled", havingValue = "true")
    public FilterRegistrationBean<Filter> removeDruidAdvertiseFilter() throws IOException {
        String text = Utils.readFromResource("support/http/resources/js/common.js");
        text = text.replaceAll("this.buildFooter\\(\\);", "");
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.addUrlPatterns("/druid/js/common.js");
        String finalText = text;
        registration.setFilter((servletRequest, servletResponse, filterChain) -> {
            servletResponse.resetBuffer();
            servletResponse.setContentType("text/javascript;charset=utf-8");
            servletResponse.getWriter().write(finalText);
        });
        return registration;
    }

}
