package com.pongsky.kit.sms.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * 阿里云 OSS 配置
 *
 * @author pengsenhao
 */
@ConditionalOnProperty(value = "aliyun.sms.enabled", havingValue = "true", matchIfMissing = true)
@Getter
@Setter
@Validated
@ConfigurationProperties("aliyun.sms")
public class AliYunSmsProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * regionId
     */
    @NotBlank
    private String regionId;

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
