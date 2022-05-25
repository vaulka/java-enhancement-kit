package com.pongsky.kit.response;

import com.pongsky.kit.exception.CircuitBreakerException;
import com.pongsky.kit.response.enums.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 响应数据体
 *
 * @author pengsenhao
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalResult<T> {

    /**
     * 【成功】构造方法
     *
     * @param data 接口响应数据体
     */
    private GlobalResult(T data) {
        this.code = ResultCode.Success.getCode();
        this.message = ResultCode.Success.getMessage();
        this.data = data;
    }

    /**
     * 【失败】构造方法
     *
     * @param ip         客户端 IP 地址
     * @param resultCode 响应数据枚举
     * @param message    异常信息
     * @param path       调用接口路径地址
     */
    private GlobalResult(String ip, ResultCode resultCode, String message, String path) {
        this.ip = ip;
        this.details = resultCode.getDetails();
        this.code = resultCode.getCode();
        this.message = message;
        this.path = path;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 【失败】构造方法
     *
     * @param ip         客户端 IP 地址
     * @param resultCode 响应数据枚举
     * @param path       调用接口路径地址
     * @param exception  异常类型信息
     */
    private GlobalResult(String ip, ResultCode resultCode, String path, Class<? extends Exception> exception) {
        this.ip = ip;
        this.details = resultCode.getDetails();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.path = path;
        this.exception = exception.getName();
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 【成功】构造方法
     *
     * @param data 接口响应数据体
     * @param <T>  范型
     * @return 成功响应体
     */
    public static <T> GlobalResult<T> success(T data) {
        return new GlobalResult<>(data);
    }

    /**
     * 【失败】构造方法
     *
     * @param ip      客户端 IP 地址
     * @param message 异常信息
     * @param path    调用接口路径地址
     * @return 失败响应体
     */
    public static GlobalResult<Void> fail(String ip,
                                          String message,
                                          String path) {
        return new GlobalResult<>(ip, ResultCode.GatewayException, message, path);
    }

    /**
     * 【失败】构造方法
     *
     * @param ip         客户端 IP 地址
     * @param resultCode 响应数据枚举
     * @param path       调用接口路径地址
     * @param exception  异常类型信息
     * @return 失败响应体
     */
    public static GlobalResult<Void> fail(String ip,
                                          ResultCode resultCode,
                                          String path,
                                          Class<? extends Exception> exception) {
        return new GlobalResult<>(ip, resultCode, path, exception);
    }

    /**
     * 熔断 响应数据
     *
     * @return 熔断 响应数据
     */
    public static GlobalResult<Void> circuitBreakerExceptionResult() {
        return new GlobalResult<>(null, ResultCode.CircuitBreakerException, null,
                CircuitBreakerException.class);
    }

    /**
     * 接口响应结果标识码
     * <p>
     * 有数据的情况：成功、失败
     */
    @ApiModelProperty("接口响应结果标识码")
    @Schema(description = "接口响应结果标识码")
    private Integer code;

    /**
     * 客户端 IP 地址
     * <p>
     * 有数据的情况：失败
     */
    @ApiModelProperty("客户端 IP 地址")
    @Schema(description = "客户端 IP 地址")
    private String ip;

    /**
     * 异常详细信息
     * <p>
     * 有数据的情况：失败
     */
    @ApiModelProperty("异常详细信息")
    @Schema(description = "异常详细信息")
    private String details;

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
     * 调用接口路径地址
     * <p>
     * 有数据的情况：失败
     */
    @ApiModelProperty("调用接口路径地址")
    @Schema(description = "调用接口路径地址")
    private String path;

    /**
     * 异常类型信息
     * <p>
     * 有数据的情况：失败
     */
    @ApiModelProperty("异常类型信息")
    @Schema(description = "异常类型信息")
    private String exception;

    /**
     * 时间戳
     * <p>
     * 有数据的情况：失败
     */
    @ApiModelProperty("时间戳")
    @Schema(description = "时间戳")
    private Long timestamp;

}
