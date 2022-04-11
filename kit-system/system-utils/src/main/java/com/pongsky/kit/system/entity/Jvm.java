package com.pongsky.kit.system.entity;

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
     * JDK 版本
     */
    private String version;

    /**
     * JVM 内存容量
     */
    private String memoryCapacity;

    /**
     * JVM 已使用内存容量
     */
    private String usedMemoryCapacity;

    /**
     * JVM 空闲内存容量
     */
    private String freeMemoryCapacity;

    /**
     * JVM 内存使用率
     */
    private String memoryUtilization;

}
