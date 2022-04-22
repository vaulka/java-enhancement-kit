package com.pongsky.kit.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * JDK 信息
 *
 * @author pengsenhao
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Jdk {

    /**
     * JDK 名称
     */
    @ApiModelProperty("JDK 名称")
    private String name;

    /**
     * JDK 版本
     */
    @ApiModelProperty("JDK 版本")
    private String version;

    /**
     * JDK 路径
     */
    @ApiModelProperty("JDK 路径")
    private String home;

    /**
     * 项目启动路径
     */
    @ApiModelProperty("项目启动路径")
    private String runHome;

}
