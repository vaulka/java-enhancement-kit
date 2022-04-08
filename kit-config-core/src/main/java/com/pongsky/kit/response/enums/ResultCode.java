package com.pongsky.kit.response.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 响应数据枚举
 *
 * @author pengsenhao
 */
@Getter
public enum ResultCode {

    // 0 表示接口成功

    Success("接口响应成功", 0, "成功"),

    // 100 ~ 199 区间表示参数错误

    DoesNotExistException("不存在异常", 100,
            Arrays.asList(com.pongsky.kit.exception.DoesNotExistException.class)),

    ValidationException("校验异常", 101,
            Arrays.asList(com.pongsky.kit.exception.ValidationException.class,
                    javax.validation.ConstraintViolationException.class)),

    BindException("param 参数异常", 102),

    MethodArgumentNotValidException("body 参数异常", 103),

    MultipartException("空文件上传异常", 104, "请选择文件进行上传",
            Arrays.asList(org.springframework.web.multipart.MultipartException.class)),

    HttpMessageNotReadableException("JSON 数据错误异常", 105, "JSON 数据错误异常"),

    ExistException("存在异常", 106,
            Arrays.asList(com.pongsky.kit.exception.ExistException.class)),

    MaxUploadSizeExceededException("文件上传大小异常", 107, "上传的文件体积超过限制，请缩小文件后重试",
            Arrays.asList(org.springframework.web.multipart.MaxUploadSizeExceededException.class)),

    IllegalArgumentException("非法参数异常", 108,
            Arrays.asList(IllegalArgumentException.class)),

    NoHandlerFoundException("API 接口不存在异常", 109),

    HttpRequestMethodNotSupportedException("API 接口方法不存在异常", 110),

    HttpException("HTTP 请求异常", 113,
            Arrays.asList(com.pongsky.kit.exception.HttpException.class)),

    GatewayException("网关拦截异常", 114),

    // 200 ~ 299 区间表示用户错误

    UserOperateException("用户操作异常", 201),

    FrequencyException("频率异常", 202,
            Arrays.asList(com.pongsky.kit.exception.FrequencyException.class)),

    // 400 ~ 499 区间表示登录态异常

    /**
     * 权限不足，禁止调无权限接口
     */
    AccessDeniedException("访问权限异常", 403, "访问凭证已过期，请重新登录",
            Arrays.asList(org.springframework.security.access.AccessDeniedException.class)),

    // 500 ~ 599 区间表示接口异常

    RuntimeException("运行时异常", 500,
            Arrays.asList(java.lang.RuntimeException.class)),

    RemoteCallException("远程调用异常", 501, "远程调用服务失败",
            Arrays.asList(com.pongsky.kit.exception.RemoteCallException.class), true),

    CircuitBreakerException("断路异常", 502, "服务器出了点小差，请稍后再试"),

    InsertException("保存异常", 503,
            Arrays.asList(com.pongsky.kit.exception.InsertException.class)),

    UpdateException("修改异常", 504,
            Arrays.asList(com.pongsky.kit.exception.UpdateException.class)),

    DeleteException("删除异常", 505,
            Arrays.asList(com.pongsky.kit.exception.DeleteException.class)),


    //1000 表示系统异常

    Exception("系统异常", 1000),

    ;

    ResultCode(String details, Integer code) {
        this.details = details;
        this.code = code;
    }

    ResultCode(String details, Integer code, String message) {
        this.details = details;
        this.code = code;
        this.message = message;
    }

    ResultCode(String details, Integer code, List<Class<? extends Exception>> exceptions) {
        this.details = details;
        this.code = code;
        this.exceptions = exceptions.stream()
                .map(Class::getName)
                .collect(Collectors.toList());
    }

    ResultCode(String details, Integer code, String message, List<Class<? extends Exception>> exceptions) {
        this.details = details;
        this.code = code;
        this.message = message;
        this.exceptions = exceptions.stream()
                .map(Class::getName)
                .collect(Collectors.toList());
    }

    ResultCode(String details, Integer code, String message, List<Class<? extends Exception>> exceptions, Boolean isThrow) {
        this.details = details;
        this.code = code;
        this.message = message;
        this.exceptions = exceptions.stream()
                .map(Class::getName)
                .collect(Collectors.toList());
        this.isThrow = isThrow;
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

    /**
     * 异常列表
     */
    private List<String> exceptions = Collections.emptyList();

    /**
     * 是否直接抛出异常
     */
    private Boolean isThrow = false;

    /**
     * 所有响应结果列表
     */
    private static final List<ResultCode> RESULTS = Arrays.stream(values())
            .collect(Collectors.toList());

    /**
     * 获取响应结果编码
     *
     * @param exception 异常
     * @return 响应结果编码
     */
    public static ResultCode getCode(Exception exception) {
        return RESULTS.stream()
                .filter(r -> r.getExceptions().contains(exception.getClass().getName()))
                .findFirst()
                .orElse(ResultCode.Exception);
    }

}
