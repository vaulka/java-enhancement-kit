package com.pongsky.kit.system.entity;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("CPU 名称")
    private String model;

    /**
     * CPU 架构
     */
    @ApiModelProperty("CPU 架构")
    private String microArchitecture;

    /**
     * CPU 内核总数
     */
    @ApiModelProperty("CPU 内核总数")
    private Integer physicalProcessorCount;

    /**
     * CPU 逻辑处理器总数
     */
    @ApiModelProperty("CPU 逻辑处理器总数")
    private Integer logicalProcessorCount;

    /**
     * CPU 系统使用率
     */
    @ApiModelProperty("CPU 系统使用率")
    private String systemUsage;

    /**
     * CPU 用户使用率
     */
    @ApiModelProperty("CPU 用户使用率")
    private String userUsage;

    /**
     * CPU 等待率
     */
    @ApiModelProperty("CPU 等待率")
    private String ioWaitUsage;

    /**
     * CPU 总使用率
     */
    @ApiModelProperty("CPU 总使用率")
    private String totalUsage;

}
