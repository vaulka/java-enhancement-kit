package com.pongsky.kit.storage.config;

import com.pongsky.kit.utils.storage.AliYunOssUtils;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.text.MessageFormat;

/**
 * 阿里云 OSS 配置
 *
 * @author pengsenhao
 */
@Setter
@ConfigurationProperties(AliYunOssConfig.PREFIX)
public class AliYunOssConfig {

    /**
     * 配置前缀
     */
    public static final String PREFIX = "aliyun.oss";

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
     * accessKeyId
     */
    private String accessKeyId;

    /**
     * accessKeySecret
     */
    private String accessKeySecret;

    /**
     * 获取阿里云 OSS 工具类
     *
     * @return 获取阿里云 OSS 工具类
     * @author pengsenhao
     */
    public AliYunOssUtils getUtils() {
        Assert.notNull(endpoint, MessageFormat.format(ERROR_MSG_FORMAT, PREFIX, "endpoint", "不能为空"));
        Assert.notNull(bucket, MessageFormat.format(ERROR_MSG_FORMAT, PREFIX, "bucket", "不能为空"));
        Assert.notNull(accessKeyId, MessageFormat.format(ERROR_MSG_FORMAT, PREFIX, "accessKeyId", "不能为空"));
        Assert.notNull(accessKeySecret, MessageFormat.format(ERROR_MSG_FORMAT, PREFIX, "accessKeySecret", "不能为空"));
        return new AliYunOssUtils(endpoint, bucket, accessKeyId, accessKeySecret);
    }

}
