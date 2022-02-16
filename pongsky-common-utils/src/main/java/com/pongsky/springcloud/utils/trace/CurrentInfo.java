package com.pongsky.springcloud.utils.trace;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * 当前请求信息
 *
 * @author pengsenhao
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CurrentInfo implements Serializable {

    private static final long serialVersionUID = -1945730749815934089L;

    /**
     * 链路ID
     */
    private String traceId;

    /**
     * Authorization
     */
    private String authorization;

    /**
     * requestHeader
     */
    private Map<String, String> requestHeaders;

}
