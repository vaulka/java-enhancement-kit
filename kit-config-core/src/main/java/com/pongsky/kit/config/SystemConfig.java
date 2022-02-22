package com.pongsky.kit.config;

import com.pongsky.kit.utils.model.emums.Active;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 系统配置文件
 *
 * @author pengsenhao
 */
@Getter
@Setter
@Component
public class SystemConfig {

    public SystemConfig() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        this.hostName = address.getHostName();
    }

    /**
     * 环境
     */
    @Value("${spring.profiles.active}")
    private Active active;

    /**
     * 应用名称
     */
    @Value("${application.name}")
    private String applicationName;

    /**
     * 服务名称
     */
    @Value("${spring.application.name}")
    private String serviceName;

    /**
     * 版本号
     */
    @Value("${application.version}")
    private String version;

    /**
     * HostName
     */
    private String hostName;

    /**
     * 实例唯一ID
     */
    @Value("${application.instance-id:${brokerId}}")
    private String instanceId;

}
