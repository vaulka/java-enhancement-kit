package com.pongsky.kit.ip.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * IP 配置
 *
 * @author pengsenhao
 */
@Getter
@Setter
@Validated
@ConfigurationProperties("ip")
public class IpProperties {

    /**
     * ip2region.xdb 文件路径
     */
    @NotBlank
    private String dbPath = "classpath:ip2region/ip2region.xdb";

}
