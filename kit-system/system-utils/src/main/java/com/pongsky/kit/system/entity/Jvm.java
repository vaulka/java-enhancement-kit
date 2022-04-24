package com.pongsky.kit.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * JVM 指标信息
 *
 * @author pengsenhao
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Jvm {

    /**
     * JVM 内存容量
     */
    @ApiModelProperty("项目启动路径")
    private String capacity;

    /**
     * JVM 已使用容量
     */
    @ApiModelProperty("JVM 已使用容量")
    private String usedCapacity;

    /**
     * JVM 空闲容量
     */
    @ApiModelProperty("JVM 空闲容量")
    private String freeCapacity;

    /**
     * JVM 使用率
     */
    @ApiModelProperty("JVM 使用率")
    private String usage;

}
