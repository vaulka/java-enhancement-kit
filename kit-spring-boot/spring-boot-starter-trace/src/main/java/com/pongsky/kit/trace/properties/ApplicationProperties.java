package com.pongsky.kit.trace.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 系统配置文件
 *
 * @author pengsenhao
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    public ApplicationProperties(@Value("${application.instance-id:${brokerId}}") String instanceId) {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
            this.hostName = address.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.instanceId = instanceId;
    }

    /**
     * 应用名称
     */
    private String name;

    /**
     * 版本号
     */
    private String version;

    /**
     * HostName
     */
    private String hostName;

    /**
     * 实例ID
     */
    private String instanceId;

}
