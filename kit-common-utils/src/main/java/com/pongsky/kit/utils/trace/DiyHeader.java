package com.pongsky.kit.utils.trace;

/**
 * 自定义 header
 * <p>
 * x- 开头
 *
 * @author pengsenhao
 **/
public interface DiyHeader {

    /**
     * HostName
     */
    String X_HOSTNAME = "X-HostName";

    /**
     * 实例ID
     */
    String X_INSTANCE_ID = "X-Instance-Id";

    /**
     * 链路ID
     */
    String X_TRACE_ID = "X-Trace-Id";

    /**
     * 后端版本号
     */
    String X_BACKEND_VERSION = "X-Backend-Version";

    /**
     * 远程调用 前缀
     */
    String REMOTE_PREFIX = "REMOTE";

    /**
     * 原始调用 前缀
     */
    String ORIGIN_PREFIX = "ORIGIN";

}