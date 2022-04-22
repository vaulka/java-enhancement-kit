package com.pongsky.kit.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Disk 指标信息
 *
 * @author pengsenhao
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Disk {

    /**
     * Disk 挂载盘符
     */
    @ApiModelProperty("Disk 挂载盘符")
    private String mount;

    /**
     * Disk 类型
     */
    @ApiModelProperty("Disk 类型")
    private String type;

    /**
     * Disk 名称
     */
    @ApiModelProperty("Disk 名称")
    private String name;

    /**
     * Disk 容量
     */
    @ApiModelProperty("Disk 容量")
    private String capacity;

    /**
     * Disk 已使用容量
     */
    @ApiModelProperty("Disk 已使用容量")
    private String usedCapacity;

    /**
     * Disk 空闲容量
     */
    @ApiModelProperty("Disk 空闲容量")
    private String freeCapacity;

    /**
     * Disk 使用率
     */
    @ApiModelProperty("Disk 使用率")
    private String usage;

}
