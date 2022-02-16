package com.pongsky.springcloud.utils.trace;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-01-24 1:50 下午
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
