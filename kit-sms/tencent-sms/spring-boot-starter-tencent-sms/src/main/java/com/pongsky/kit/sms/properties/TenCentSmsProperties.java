package com.pongsky.kit.sms.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * 腾讯云 OSS 配置
 *
 * @author pengsenhao
 */
@ConditionalOnProperty(value = "tencent.sms.enabled", havingValue = "true", matchIfMissing = true)
@Getter
@Setter
@Validated
@ConfigurationProperties("tencent.sms")
public class TenCentSmsProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * region
     */
    @NotBlank
    private String region;

    /**
     * sdkAppId
     */
    @NotBlank
    private String sdkAppId;

    /**
     * secretId
     */
    @NotBlank
    private String secretId;

    /**
     * secretKey
     */
    @NotBlank
    private String secretKey;

}
