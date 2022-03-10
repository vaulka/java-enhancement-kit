package com.pongsky.kit.storage.config;

import com.pongsky.kit.utils.storage.MinIoUtils;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.text.MessageFormat;

/**
 * MinIO 配置
 *
 * @author pengsenhao
 */
@Setter
@ConfigurationProperties(MinIoConfig.PREFIX)
public class MinIoConfig {

    /**
     * 配置前缀
     */
    public static final String PREFIX = "minio";

    /**
     * 错误信息格式
     */
    private static final String ERROR_MSG_FORMAT = "yml 配置 {0}.{1} {2}";


    /**
     * endpoint
     */
    private String endpoint;

    /**
     * bucket
     */
    private String bucket;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 获取 MinIO 工具类
     *
     * @return 获取 MinIO 工具类
     * @author pengsenhao
     */
    public MinIoUtils getUtils() {
        Assert.notNull(endpoint, MessageFormat.format(ERROR_MSG_FORMAT, PREFIX, "endpoint", "不能为空"));
        Assert.notNull(bucket, MessageFormat.format(ERROR_MSG_FORMAT, PREFIX, "bucket", "不能为空"));
        Assert.notNull(accessKey, MessageFormat.format(ERROR_MSG_FORMAT, PREFIX, "accessKey", "不能为空"));
        Assert.notNull(secretKey, MessageFormat.format(ERROR_MSG_FORMAT, PREFIX, "secretKey", "不能为空"));
        return new MinIoUtils(endpoint, bucket, accessKey, secretKey);
    }

}
