package com.pongsky.kit.system.entity;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * OS 信息
 *
 * @author pengsenhao
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Os {

    /**
     * 操作系统名称
     */
    @ApiModelProperty("操作系统名称")
    @Schema(description = "操作系统名称")
    private String name;

    /**
     * 操作系统架构
     */
    @ApiModelProperty("操作系统架构")
    @Schema(description = "操作系统架构")
    private String arch;

    /**
     * 主机名称
     */
    @ApiModelProperty("主机名称")
    @Schema(description = "主机名称")
    private String hostName;

    /**
     * 主机 IP 地址
     */
    @ApiModelProperty("主机 IP 地址")
    @Schema(description = "主机 IP 地址")
    private String hostAddress;

}
