package com.pongsky.kit.common.response;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应数据体
 *
 * @author pengsenhao
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class GlobalResult<T> implements Serializable {

    private static final long serialVersionUID = -8691252594451970912L;

    /**
     * 成功 code
     */
    public static final int SUCCESS_CODE = 0;

    /**
     * 成功 message
     */
    public static final String SUCCESS_MESSAGE = "成功";

    /**
     * 接口响应结果标识码
     * <p>
     * 有数据的情况：成功、失败
     */
    @ApiModelProperty("接口响应结果标识码")
    @Schema(description = "接口响应结果标识码")
    private Integer code;

    /**
     * 接口响应结果信息
     * <p>
     * 有数据的情况：成功、失败
     */
    @ApiModelProperty("接口响应结果信息")
    @Schema(description = "接口响应结果信息")
    private String message;

    /**
     * 接口响应数据体
     * <p>
     * 有数据的情况：成功
     */
    @ApiModelProperty("接口响应数据体")
    @Schema(description = "接口响应数据体")
    private T data;

    /**
     * 异常类型信息
     * <p>
     * 有数据的情况：失败
     */
    @ApiModelProperty("异常类型信息")
    @Schema(description = "异常类型信息")
    private String type;

    /**
     * 异常类信息
     * <p>
     * 有数据的情况：失败
     */
    @ApiModelProperty("异常类信息")
    @Schema(description = "异常类信息")
    private String exception;

    /**
     * 客户端 IP 地址
     * <p>
     * 有数据的情况：失败
     */
    @ApiModelProperty("客户端 IP 地址")
    @Schema(description = "客户端 IP 地址")
    private String ip;

    /**
     * 调用接口路径地址
     * <p>
     * 有数据的情况：失败
     */
    @ApiModelProperty("调用接口路径地址")
    @Schema(description = "调用接口路径地址")
    private String path;

    /**
     * 时间戳
     * <p>
     * 有数据的情况：失败
     */
    @ApiModelProperty("时间戳")
    @Schema(description = "时间戳")
    private Long timestamp;

}
