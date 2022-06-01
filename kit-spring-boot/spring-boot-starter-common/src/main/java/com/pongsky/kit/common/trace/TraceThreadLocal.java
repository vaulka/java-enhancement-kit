package com.pongsky.kit.common.trace;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * 当前请求信息配置
 *
 * @author pengsenhao
 */
public class TraceThreadLocal {

    /**
     * 当前线程的链路信息
     */
    private static final ThreadLocal<TraceInfo> TRACE_INFO = new ThreadLocal<>();

    /**
     * 链路ID 格式
     */
    private static final String TRACE_ID_FORMAT = "{0}-{1}";

    /**
     * 构建链路ID
     *
     * @param request request
     * @return 链路ID
     */
    public static String buildTraceId(HttpServletRequest request) {
        String traceId;
        if (StringUtils.isNotBlank(request.getHeader(TraceConstants.X_TRACE_ID))) {
            traceId = request.getHeader(TraceConstants.X_TRACE_ID);
        } else if (StringUtils.isNotBlank(TraceThreadLocal.getTraceId())) {
            traceId = TraceThreadLocal.getTraceId();
        } else {
            traceId = MessageFormat.format(TRACE_ID_FORMAT,
                    TraceConstants.ORIGIN_PREFIX,
                    UUID.randomUUID().toString().replaceAll("-", ""));
        }
        return traceId;
    }

    /**
     * 获取链路信息
     *
     * @return 获取链路信息
     */
    public static TraceInfo getTraceInfo() {
        return TRACE_INFO.get();
    }

    /**
     * 获取 链路ID
     *
     * @return 链路ID
     */
    public static String getTraceId() {
        TraceInfo traceInfo = TraceThreadLocal.getTraceInfo();
        if (traceInfo == null) {
            return null;
        }
        return traceInfo.getTraceId();
    }

    /**
     * 获取 Authorization
     *
     * @return Authorization
     */
    public static String getAuthorization() {
        TraceInfo traceInfo = TraceThreadLocal.getTraceInfo();
        if (traceInfo == null) {
            return null;
        }
        return traceInfo.getAuthorization();
    }

    /**
     * 保存 链路信息
     *
     * @param traceInfo 链路信息
     */
    public static void setTraceInfo(TraceInfo traceInfo) {
        TRACE_INFO.set(traceInfo);
    }

    /**
     * 删除 链路信息
     */
    public static void delTraceInfo() {
        TRACE_INFO.remove();
    }

}
