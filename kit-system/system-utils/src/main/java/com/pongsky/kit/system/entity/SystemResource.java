package com.pongsky.kit.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

/**
 * 系统资源信息
 *
 * @author pengsenhao
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SystemResource {

    /**
     * OS 信息
     */
    @ApiModelProperty("OS 信息")
    private Os os;

    /**
     * CPU 指标信息
     */
    @ApiModelProperty("CPU 指标信息")
    private Cpu cpu;

    /**
     * MEM 指标信息
     */
    @ApiModelProperty("MEM 指标信息")
    private Mem mem;

    /**
     * JDK 信息
     */
    @ApiModelProperty("JDK 信息")
    private Jdk jdk;

    /**
     * JVM 指标信息
     */
    @ApiModelProperty("JVM 指标信息")
    private Jvm jvm;

    /**
     * Disk 信息
     */
    @ApiModelProperty("Disk 信息")
    private List<Disk> disks = Collections.emptyList();

}
