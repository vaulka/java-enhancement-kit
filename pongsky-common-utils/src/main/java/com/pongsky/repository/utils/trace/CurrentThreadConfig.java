package com.pongsky.repository.utils.trace;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.UUID;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-01-21 10:38 上午
 */
@Data
public class CurrentThreadConfig {

    /**
     * 当前线程的请求信息
     */
    private static final ThreadLocal<CurrentInfo> CURRENT_INFO_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 链路ID 格式
     */
    private static final String TRACE_ID_FORMAT = "{0}-{1}";

    /**
     * 构建链路ID
     *
     * @param request request
     * @return 链路ID
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-01-21 10:58 上午
     */
    public static String buildTraceId(HttpServletRequest request) {
        String traceId;
        if (StringUtils.isNotBlank(request.getHeader(DiyHeader.X_TRACE_ID))) {
            traceId = request.getHeader(DiyHeader.X_TRACE_ID);
        } else if (StringUtils.isNotBlank(CurrentThreadConfig.getTraceId())) {
            traceId = CurrentThreadConfig.getTraceId();
        } else {
            traceId = MessageFormat.format(TRACE_ID_FORMAT,
                    DiyHeader.ORIGIN_PREFIX,
                    UUID.randomUUID().toString().replaceAll("-", ""));
        }
        return traceId;
    }

    /**
     * 获取请求信息
     *
     * @return 获取请求信息
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-01-21 10:44 上午
     */
    public static CurrentInfo getCurrentInfo() {
        return CURRENT_INFO_THREAD_LOCAL.get();
    }

    /**
     * 获取 链路ID
     *
     * @return 链路ID
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-01-24 3:17 下午
     */
    public static String getTraceId() {
        CurrentInfo currentInfo = getCurrentInfo();
        if (currentInfo == null) {
            return null;
        }
        return currentInfo.getTraceId();
    }

    /**
     * 获取 Authorization
     *
     * @return Authorization
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-01-24 3:17 下午
     */
    public static String getAuthorization() {
        CurrentInfo currentInfo = getCurrentInfo();
        if (currentInfo == null) {
            return null;
        }
        return currentInfo.getAuthorization();
    }

    /**
     * 设置 请求信息
     *
     * @param currentInfo 请求信息
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-01-21 10:45 上午
     */
    public static void setCurrentInfo(CurrentInfo currentInfo) {
        CURRENT_INFO_THREAD_LOCAL.set(currentInfo);
    }

    /**
     * 删除 请求信息
     *
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-01-21 10:46 上午
     */
    public static void relCurrentInfo() {
        CURRENT_INFO_THREAD_LOCAL.remove();
    }

}
