package com.pongsky.kit.storage.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * MinIO 配置
 *
 * @author pengsenhao
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

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
     * accessKey
     */
    @NotBlank
    private String accessKey;

    /**
     * secretKey
     */
    @NotBlank
    private String secretKey;

}
