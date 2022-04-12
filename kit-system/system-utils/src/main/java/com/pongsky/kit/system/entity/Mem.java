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
     * 主机 容量
     */
    private String capacity;

    /**
     * 主机 已使用容量
     */
    private String usedCapacity;

    /**
     * 主机 空闲容量
     */
    private String freeCapacity;

    /**
     * 主机 使用率
     */
    private String usage;

}
