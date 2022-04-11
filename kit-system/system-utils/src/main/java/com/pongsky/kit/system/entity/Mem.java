package com.pongsky.kit.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * MEM 指标信息
 *
 * @author pengsenhao
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Mem {

    /**
     * 主机 内存容量
     */
    private String memoryCapacity;

    /**
     * 主机 已使用内存容量
     */
    private String usedMemoryCapacity;

    /**
     * 主机 空闲内存容量
     */
    private String freeMemoryCapacity;

    /**
     * 主机 内存使用率
     */
    private String memoryUtilization;

}
