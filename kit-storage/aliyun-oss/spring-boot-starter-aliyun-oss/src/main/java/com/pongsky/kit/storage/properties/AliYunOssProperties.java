package com.pongsky.kit.storage.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * 阿里云 OSS 配置
 *
 * @author pengsenhao
 */
@Getter
@Setter
@Validated
@ConfigurationProperties("aliyun.oss")
public class AliYunOssProperties {

    /**
     * endpoint
     */
    @NotBlank
    private String endpoint;

    /**
     * bucket
     */
    @NotBlank
    private String bucket;

    /**
     * accessKeyId
     */
    @NotBlank
    private String accessKeyId;

    /**
     * accessKeySecret
     */
    @NotBlank
    private String accessKeySecret;

}
