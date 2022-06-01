package com.pongsky.kit.common.trace;

/**
 * 链路常量
 *
 * @author pengsenhao
 **/
public interface TraceConstants {

    /**
     * HostName 请求头
     */
    String X_HOSTNAME = "X-HostName";

    /**
     * 实例ID 请求头
     */
    String X_INSTANCE_ID = "X-Instance-Id";

    /**
     * 链路ID 请求头
     */
    String X_TRACE_ID = "X-Trace-Id";

    /**
     * 后端版本号 请求头
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
