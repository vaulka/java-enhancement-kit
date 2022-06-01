package com.pongsky.kit.common.trace;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * 链路信息
 *
 * @author pengsenhao
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TraceInfo implements Serializable {

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
     * 请求头列表
     */
    private Map<String, String> requestHeaders = Collections.emptyMap();

}
