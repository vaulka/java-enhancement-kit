package com.pongsky.kit.dynamic.datasource.core;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

/**
 * 动态数据源
 *
 * @author pengsenhao
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {

    public DynamicDataSource(Object defaultDataSource, Map<Object, Object> multiDatasets) {
        // 设置默认数据源
        super.setDefaultTargetDataSource(defaultDataSource);
        // 设置全部数据源
        super.setTargetDataSources(multiDatasets);
        super.afterPropertiesSet();
    }

    /**
     * 重写路由策略，进行数据源动态切换
     *
     * @return 即将使用的数据源
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.get();
    }

}
