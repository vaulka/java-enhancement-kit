package com.pongsky.kit.system.entity;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @ApiModelProperty("JVM 内存容量")
    @Schema(description = "JVM 内存容量")
    private String capacity;

    /**
     * JVM 已使用容量
     */
    @ApiModelProperty("JVM 已使用容量")
    @Schema(description = "JVM 已使用容量")
    private String usedCapacity;

    /**
     * JVM 空闲容量
     */
    @ApiModelProperty("JVM 空闲容量")
    @Schema(description = "JVM 空闲容量")
    private String freeCapacity;

    /**
     * JVM 使用率
     */
    @ApiModelProperty("JVM 使用率")
    @Schema(description = "JVM 使用率")
    private String usage;

}
