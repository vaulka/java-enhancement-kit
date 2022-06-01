package com.pongsky.kit.global.response.handler;

import com.pongsky.kit.global.response.handler.processor.fail.BindExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.HttpMessageNotReadableExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.HttpRequestMethodNotSupportedExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.MissingServletRequestParameterExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.NoHandlerFoundExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.TypeMismatchExceptionFailProcessor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.ApplicationContext;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 *
 * @author pengsenhao
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ApplicationContext applicationContext;

    /**
     * param 数据校验异常
     * <p>
     * {@link org.springframework.validation.annotation.Validated} 参数校验未通过
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return param 数据校验异常
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleBindException(@NonNull BindException ex,
                                                         @NonNull HttpHeaders headers,
                                                         @NonNull HttpStatus status,
                                                         @NonNull WebRequest request) {
        BindExceptionFailProcessor processor = applicationContext
                .getBean(BindExceptionFailProcessor.class);
        Object result = processor.exec(ex);
        return new ResponseEntity<>(result, processor.httpStatus());
    }

    /**
     * param 数据校验异常
     * <p>
     * {@link org.springframework.web.bind.annotation.RequestParam} 参数校验未通过
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return param 数据校验异常
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(@NonNull MissingServletRequestParameterException ex,
                                                                          @NonNull HttpHeaders headers,
                                                                          @NonNull HttpStatus status,
                                                                          @NonNull WebRequest request) {
        MissingServletRequestParameterExceptionFailProcessor processor = applicationContext
                .getBean(MissingServletRequestParameterExceptionFailProcessor.class);
        Object result = processor.exec(ex);
        return new ResponseEntity<>(result, processor.httpStatus());
    }

    /**
     * request body 数据校验异常
     * <p>
     * {@link org.springframework.validation.annotation.Validated} + {@link org.springframework.web.bind.annotation.RequestBody} 参数校验未通过
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return request body 数据校验异常
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        BindExceptionFailProcessor processor = applicationContext
                .getBean(BindExceptionFailProcessor.class);
        Object result = processor.exec(ex);
        return new ResponseEntity<>(result, processor.httpStatus());
    }

    /**
     * request body 数据转换异常
     * <p>
     * request body 数据转换异常 {@link org.springframework.web.bind.annotation.RequestBody} 所标注的数据类型
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return request body 数据转换异常
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        HttpMessageNotReadableExceptionFailProcessor processor = applicationContext
                .getBean(HttpMessageNotReadableExceptionFailProcessor.class);
        Object result = processor.exec(ex);
        return new ResponseEntity<>(result, processor.httpStatus());
    }

    /**
     * 接口不存在异常
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return 接口不存在异常
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(@NonNull NoHandlerFoundException ex,
                                                                   @NonNull HttpHeaders headers,
                                                                   @NonNull HttpStatus status,
                                                                   @NonNull WebRequest request) {
        NoHandlerFoundExceptionFailProcessor processor = applicationContext
                .getBean(NoHandlerFoundExceptionFailProcessor.class);
        Object result = processor.exec(ex);
        return new ResponseEntity<>(result, processor.httpStatus());
    }

    /**
     * 方法不存在异常
     *
     * @param ex      ex
     * @param headers headers
     * @param status  status
     * @param request request
     * @return 方法不存在异常
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(@NonNull HttpRequestMethodNotSupportedException ex,
                                                                         @NonNull HttpHeaders headers,
                                                                         @NonNull HttpStatus status,
                                                                         @NonNull WebRequest request) {
        HttpRequestMethodNotSupportedExceptionFailProcessor processor = applicationContext
                .getBean(HttpRequestMethodNotSupportedExceptionFailProcessor.class);
        Object result = processor.exec(ex);
        return new ResponseEntity<>(result, processor.httpStatus());
    }

    /**
     * 数据类型转换异常
     * <p>
     * 譬如：query 请求参数是 Integer 类型，数据传 String 类型
     *
     * @param ex      ex
     * @param headers headers
     * @param status  stats
     * @param request request
     * @return 数据类型转换异常
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(@NonNull TypeMismatchException ex,
                                                        @NonNull HttpHeaders headers,
                                                        @NonNull HttpStatus status,
                                                        @NonNull WebRequest request) {
        TypeMismatchExceptionFailProcessor processor = applicationContext
                .getBean(TypeMismatchExceptionFailProcessor.class);
        Object result = processor.exec(ex);
        return new ResponseEntity<>(result, processor.httpStatus());
    }

    /**
     * 全局异常处理器
     *
     * @param exception 异常
     * @param request   request
     * @return 响应结果
     */
    @ExceptionHandler(value = Throwable.class)
    public String exceptionHandler(Throwable exception, HttpServletRequest request) {
        request.setAttribute(GlobalExceptionHandler.class.getName(), exception);
        return exception.getLocalizedMessage();
    }

}
