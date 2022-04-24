package com.pongsky.kit.dynamic.datasource.core;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 动态数据源上下文信息
 *
 * @author pengsenhao
 **/
public class DynamicDataSourceContextHolder {

    /**
     * 默认数据源名称
     */
    public static final String DEFAULT_DATA_SOURCE_NAME = "default";

    /**
     * 数据源名称列表
     */
    private static final List<String> DATA_SOURCE_NAMES = new CopyOnWriteArrayList<>();

    /**
     * 数据源组名关联关系
     */
    private static final Map<String, List<String>> DATA_SOURCE_GROUP = new ConcurrentHashMap<>(16);

    /**
     * 间隔符
     */
    private static final String SPACER = "-";

    /**
     * 设置数据源名称列表、数据源组名关联关系
     *
     * @param dataSourceNames 数据源名称列表
     */
    public static void setDataSourceNames(List<String> dataSourceNames) {
        // 设置数据源名称列表
        DATA_SOURCE_NAMES.addAll(dataSourceNames);
        // 设置数据源组名关联关系
        List<String> dataSourceGroups = dataSourceNames.stream()
                .filter(dsn -> dsn.contains(SPACER))
                .map(dsn -> dsn.split(SPACER)[0])
                .collect(Collectors.toList());
        for (String dataSourceGroup : dataSourceGroups) {
            List<String> dataSourceGroupNames = dataSourceNames.stream()
                    .filter(dsn -> dsn.startsWith(dataSourceGroup))
                    .collect(Collectors.toList());
            DATA_SOURCE_GROUP.put(dataSourceGroup, dataSourceGroupNames);
        }
    }

    /**
     * 每个线程独立设置动态数据源信息
     */
    private static final ThreadLocal<String> CURRENT_DATA_SOURCE_NAME = new ThreadLocal<>();

    /**
     * 随机数
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * 获取数据源名称信息
     *
     * @return 获取数据源名称信息
     */
    public static String get() {
        String dataSourceName = CURRENT_DATA_SOURCE_NAME.get();
        // 如果没配置，则使用默认数据源名称
        if (dataSourceName == null) {
            return DEFAULT_DATA_SOURCE_NAME;
        }
        // 如果是数据源名称，则直接返回
        if (DATA_SOURCE_NAMES.contains(dataSourceName)) {
            return dataSourceName;
        }
        // 如果是组名，则直接返回
        List<String> dataSourceNames = DATA_SOURCE_GROUP.get(dataSourceName);
        if (dataSourceNames != null) {
            int randomNum = SECURE_RANDOM.nextInt(dataSourceNames.size());
            return dataSourceNames.get(randomNum);
        }
        return DEFAULT_DATA_SOURCE_NAME;
    }

    /**
     * 设置数据源名称信息
     *
     * @param datasourceName 设置数据源名称信息
     */
    public static void set(String datasourceName) {
        CURRENT_DATA_SOURCE_NAME.set(datasourceName);
    }

    /**
     * 删除数据源名称信息
     */
    public static void clear() {
        CURRENT_DATA_SOURCE_NAME.remove();
    }

}
