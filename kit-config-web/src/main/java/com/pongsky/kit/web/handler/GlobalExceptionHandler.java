package com.pongsky.kit.web.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pongsky.kit.ip.utils.IpUtils;
import com.pongsky.kit.response.GlobalResult;
import com.pongsky.kit.response.enums.ResultCode;
import com.pongsky.kit.validation.utils.ValidationUtils;
import com.pongsky.kit.web.request.RequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 全局异常处理
 *
 * @author pengsenhao
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper jsonMapper;

    /**
     * 校验 param 数据异常
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return 校验 param 数据异常
     */
    @Nonnull
    @Override
    protected ResponseEntity<Object> handleBindException(@Nonnull BindException ex,
                                                         @Nonnull HttpHeaders headers,
                                                         @Nonnull HttpStatus status,
                                                         @Nonnull WebRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                (RequestContextHolder.currentRequestAttributes())).getRequest();
        Object result = this.getResult(ResultCode.BindException, ValidationUtils.getErrorMessage(ex.getBindingResult()),
                ex, httpServletRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 校验 param 数据异常
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return 校验 param 数据异常
     */
    @Nonnull
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(@Nonnull MissingServletRequestParameterException ex,
                                                                          @Nonnull HttpHeaders headers,
                                                                          @Nonnull HttpStatus status,
                                                                          @Nonnull WebRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                (RequestContextHolder.currentRequestAttributes())).getRequest();
        Object result = this.getResult(ResultCode.BindException, ex.getMessage(), ex, httpServletRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 校验 body 数据异常
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return 校验 body 数据异常
     */
    @Nonnull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@Nonnull MethodArgumentNotValidException ex,
                                                                  @Nonnull HttpHeaders headers,
                                                                  @Nonnull HttpStatus status,
                                                                  @Nonnull WebRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                (RequestContextHolder.currentRequestAttributes())).getRequest();
        Object result = this.getResult(ResultCode.MethodArgumentNotValidException,
                ValidationUtils.getErrorMessage(ex.getBindingResult()), ex, httpServletRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * JSON 数据错误异常
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return JSON 数据错误异常
     */
    @Nonnull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@Nonnull HttpMessageNotReadableException ex,
                                                                  @Nonnull HttpHeaders headers,
                                                                  @Nonnull HttpStatus status,
                                                                  @Nonnull WebRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                (RequestContextHolder.currentRequestAttributes())).getRequest();
        Object result = this.getResult(ResultCode.HttpMessageNotReadableException, null, ex, httpServletRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 接口不存在异常
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return 接口不存在异常
     * @author pengsenhao
     */
    @Nonnull
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(@Nonnull NoHandlerFoundException ex,
                                                                   @Nonnull HttpHeaders headers,
                                                                   @Nonnull HttpStatus status,
                                                                   @Nonnull WebRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                (RequestContextHolder.currentRequestAttributes())).getRequest();
        Object result = this.getResult(ResultCode.NoHandlerFoundException,
                httpServletRequest.getRequestURI() + " 接口不存在", ex, httpServletRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 方法不存在异常
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return 方法不存在异常
     * @author pengsenhao
     */
    @Nonnull
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(@Nonnull HttpRequestMethodNotSupportedException ex,
                                                                         @Nonnull HttpHeaders headers,
                                                                         @Nonnull HttpStatus status,
                                                                         @Nonnull WebRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                (RequestContextHolder.currentRequestAttributes())).getRequest();
        Object result = this.getResult(ResultCode.HttpRequestMethodNotSupportedException,
                httpServletRequest.getMethod() + " 方法不存在", ex, httpServletRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 异常处理器
     *
     * @param exception exception
     * @param request   request
     * @return 响应结果
     */
    @ExceptionHandler(value = Exception.class)
    public Object exceptionHandler(Exception exception, HttpServletRequest request) {
        ResultCode resultCode = ResultCode.getCode(exception);
        if (resultCode.getIsThrow()) {
            return exception;
        }
        return this.getResult(resultCode, resultCode.getMessage(), exception, request);
    }

    /**
     * 封装异常响应体并打印
     *
     * @param resultCode resultCode
     * @param message    message
     * @param exception  exception
     * @param request    request
     * @return 封装异常响应体并打印
     */
    protected Object getResult(ResultCode resultCode, String message, Exception exception, HttpServletRequest request) {
        String ip = IpUtils.getIp(request);
        // 可通过 getAttribute 获取自定义注解对 body 数据对特定业务场景进行特殊处理
        GlobalResult<Void> result = GlobalResult.fail(ip, resultCode, request.getRequestURI(), exception.getClass());
        exception = this.getException(exception, 0);
        if (message != null) {
            result.setMessage(message);
        } else if (result.getMessage() == null) {
            if (exception.getLocalizedMessage() != null) {
                result.setMessage(exception.getLocalizedMessage());
            } else if (exception.getMessage() != null) {
                result.setMessage(exception.getMessage());
            }
        }
        this.log(exception, request, result);
        return result;
    }

    /**
     * 异常递归次数，防止堆溢出
     */
    private static final int THROWABLE_COUNT = 10;

    /**
     * 获取最底层的异常
     *
     * @param exception 异常
     * @param number    次数
     * @return 获取最底层的异常
     */
    private Exception getException(Exception exception, int number) {
        if (number > THROWABLE_COUNT) {
            return exception;
        }
        if (exception.getCause() != null) {
            return this.getException(exception, ++number);
        }
        return exception;
    }

    /**
     * 打印堆栈信息最小标识码
     */
    private static final int BOUNDARY = 500;

    /**
     * 打印日志详细信息
     *
     * @param exception 异常
     * @param request   request
     * @param result    错误响应数据
     */
    private void log(Exception exception, HttpServletRequest request, GlobalResult<Void> result) {
        log.error("");
        log.error("Started exception");
        String ip = IpUtils.getIp(request);
        String userAgent = Optional.ofNullable(request.getHeader(HttpHeaders.USER_AGENT)).orElse("");
        String referer = Optional.ofNullable(request.getHeader(HttpHeaders.REFERER)).orElse("");
        log.error("request header: IP [{}] userAgent [{}] referer [{}]", ip, userAgent, referer);
        if (result.getCode() >= BOUNDARY) {
            log.error("request: methodURL [{}] methodType [{}] params [{}] body [{}]",
                    request.getRequestURI(),
                    request.getMethod(),
                    Optional.ofNullable(request.getQueryString()).orElse(""),
                    Optional.ofNullable(RequestUtils.getBody(request)).orElse(""));
            log.error("exception message: [{}]", result.getMessage());
            exception.printStackTrace();
        } else {
            log.error("exception message: [{}]", result.getMessage());
        }
        try {
            log.error("response: [{}]", jsonMapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
        }
        log.error("Ended exception");
    }

}
