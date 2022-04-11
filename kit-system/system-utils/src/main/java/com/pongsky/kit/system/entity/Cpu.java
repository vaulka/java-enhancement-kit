package com.pongsky.kit.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * CPU 指标信息
 *
 * @author pengsenhao
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Cpu {

    /**
     * CPU 名称
     */
    private String name;

    /**
     * CPU 架构
     */
    private String microArchitecture;

    /**
     * CPU 内核总数
     */
    private Integer physicalProcessorCount;

    /**
     * CPU 逻辑处理器总数
     */
    private Integer logicalProcessorCount;

    /**
     * CPU 系统使用率
     */
    private String systemUtilization;

    /**
     * CPU 用户使用率
     */
    private String userUtilization;

    /**
     * CPU 等待率
     */
    private String ioWaitRate;

    /**
     * CPU 总使用率
     */
    private String totalUtilization;

}
