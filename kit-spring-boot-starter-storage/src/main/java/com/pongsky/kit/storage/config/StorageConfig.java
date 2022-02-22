package com.pongsky.kit.storage.config;

import com.pongsky.kit.utils.storage.StorageType;
import com.pongsky.kit.utils.storage.StorageUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 云存储配置
 *
 * @author pengsenhao
 */
@Setter
@RequiredArgsConstructor
@ConfigurationProperties("storage")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({AliYunOssConfig.class, MinIoConfig.class})
public class StorageConfig {

    private final MinIoConfig minIoConfig;
    private final AliYunOssConfig aliYunOssConfig;

    /**
     * 文件基地址
     */
    @Getter
    private String baseUri = "";

    /**
     * 云存储类型
     */
    private StorageType type = StorageType.none;

    /**
     * 获取云存储工具类
     *
     * @return 获取云存储工具类
     * @author pengsenhao
     */
    public StorageUtils getUtils() {
        switch (this.type) {
            case aliyun:
                return aliYunOssConfig.getUtils();
            case minio:
                return minIoConfig.getUtils();
            case none:
            default:
                return null;
        }
    }

}
