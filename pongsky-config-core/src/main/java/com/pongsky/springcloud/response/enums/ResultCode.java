package com.pongsky.springcloud.response.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应数据枚举
 *
 * @author pengsenhao
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    // 0 表示接口成功

    Success("接口响应成功", 0, "成功"),

    // 100 ~ 199 区间表示参数错误

    DoesNotExistException("不存在异常", 100),

    ValidationException("校验异常", 101),

    BindException("param 参数异常", 102),

    MethodArgumentNotValidException("body 参数异常", 103),

    MultipartException("空文件上传异常", 104, "请选择文件进行上传"),

    HttpMessageNotReadableException("JSON 数据错误异常", 105, "JSON 数据错误异常"),

    ExistException("存在异常", 106),

    MaxUploadSizeExceededException("文件上传大小异常", 107),

    IllegalArgumentException("非法参数异常", 108),

    NoHandlerFoundException("API 接口不存在异常", 109),

    HttpRequestMethodNotSupportedException("API 接口方法不存在异常", 110),

    HttpException("HTTP 请求异常", 113),

    GatewayException("网关拦截异常", 114),

    // 200 ~ 299 区间表示用户错误

    UserOperateException("用户操作异常", 201),

    FrequencyException("频率异常", 202),

    // 400 ~ 499 区间表示登录态异常

    /**
     * 权限不足，禁止调无权限接口
     */
    AccessDeniedException("访问权限异常", 403),

    /**
     * 需重新登录/刷新Token
     */
    TokenExpiredException("Token 过期异常", 405),

    // 500 ~ 599 区间表示接口异常

    DbActionExecutionException("数据库异常", 500),

    InsertException("保存异常", 501),

    UpdateException("修改异常", 502),

    DeleteException("删除异常", 503),

    RemoteCallException("远程调用异常", 504, "远程调用服务失败"),

    CircuitBreakerException("断路异常", 505, "服务器出了点小差，请稍后再试"),

    //1000 表示系统异常

    Exception("系统异常", 1000);

    ResultCode(String details, Integer code) {
        this.details = details;
        this.code = code;
    }


    /**
     * 异常详细信息
     */
    private final String details;

    /**
     * 接口响应结果标识码
     */
    private final Integer code;

    /**
     * 接口响应结果信息
     */
    private String message;

}
