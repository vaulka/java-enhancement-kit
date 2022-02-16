package com.pongsky.springcloud.config;

import com.pongsky.springcloud.utils.model.emums.Active;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 系统配置文件
 *
 * @author pengsenhao
 */
@Component
public class SystemConfig {

    public SystemConfig() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        SystemConfig.hostName = address.getHostName();
    }

    /**
     * 环境
     */
    private static Active active;

    @Value("${spring.profiles.active}")
    public void setActive(Active active) {
        SystemConfig.active = active;
    }

    public static Active getActive() {
        return active;
    }

    /**
     * 应用名称
     */
    private static String applicationName;

    @Value("${application.name}")
    public void setApplicationName(String applicationName) {
        SystemConfig.applicationName = applicationName;
    }

    public static String getApplicationName() {
        return applicationName;
    }

    /**
     * 服务名称
     */
    private static String serviceName;

    @Value("${spring.application.name}")
    public void setServiceName(String serviceName) {
        SystemConfig.serviceName = serviceName;
    }

    public static String getServiceName() {
        return serviceName;
    }

    /**
     * 版本号
     */
    private static String version;

    @Value("${application.version}")
    public void setVersion(String version) {
        SystemConfig.version = version;
    }

    public static String getVersion() {
        return version;
    }

    /**
     * HostName
     */
    private static String hostName;

    public static String getHostName() {
        return hostName;
    }

    /**
     * 实例唯一ID
     */
    private static String instanceId;

    @Value("${application.instance-id}")
    public void setInstanceId(String instanceId) {
        SystemConfig.instanceId = instanceId;
    }

    public static String getInstanceId() {
        return instanceId;
    }

}

