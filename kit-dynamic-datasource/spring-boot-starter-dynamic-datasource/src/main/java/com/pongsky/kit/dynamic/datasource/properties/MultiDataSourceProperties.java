package com.pongsky.kit.dynamic.datasource.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 多数据源配置
 *
 * @author pengsenhao
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "spring")
public class MultiDataSourceProperties {

    /**
     * 多数据源配置
     * K：数据源名称/组名，组名根据 - 符号间隔开，譬如：master-1
     * V：数据源配置
     */
    private Map<String, DataSourceProperties> multiDatasets;

    /**
     * 数据源配置
     */
    @Getter
    @Setter
    public static class DataSourceProperties {

        /**
         * 数据库驱动程序
         */
        private String driverClassName;

        /**
         * 数据库 URL
         */
        private String url;

        /**
         * 数据库账号
         */
        private String username;

        /**
         * 数据库密码
         */
        private String password;

    }

}
