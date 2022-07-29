package com.pongsky.kit.global.response.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pongsky.kit.common.global.response.processor.fail.BaseFailAroundProcessor;
import com.pongsky.kit.common.global.response.processor.fail.BaseFailProcessor;
import com.pongsky.kit.common.global.response.processor.success.BaseSuccessAroundProcessor;
import com.pongsky.kit.common.global.response.processor.success.BaseSuccessProcessor;
import com.pongsky.kit.common.global.response.processor.supports.BaseSupportsReturnTypeProcessor;
import com.pongsky.kit.common.utils.SpringUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Spring MVC 全局响应处理器
 * <p>
 * code 标识如下：
 * <p>
 * 0：接口请求成功， gt; 0 接口请求失败
 * <p>
 * 101 ~ 199：Java、Spring MVC 内置异常
 * <p>
 * 201 ~ 299：操作异常
 * <p>
 * 301 ~ 399：用户行为异常
 * <p>
 * 401 ~ 499：登录态异常
 * <p>
 * 500 ~ 599：接口异常
 * <p>
 * 1000：系统内部异常
 *
 * @author pengsenhao
 */
@Slf4j
@RestControllerAdvice
public class GlobalResponseHandler extends RequestResponseBodyMethodProcessor implements AsyncHandlerMethodReturnValueHandler {

    private final ObjectMapper jsonMapper;

    public GlobalResponseHandler(MappingJackson2HttpMessageConverter converter, ObjectMapper jsonMapper) {
        super(Collections.singletonList(converter));
        this.jsonMapper = jsonMapper;
    }

    @Override
    public boolean isAsyncReturnValue(Object returnValue, @NonNull MethodParameter returnType) {
        return supportsReturnType(returnType);
    }

    /**
     * 是否执行全局响应处理器列表
     */
    private static final List<BaseSupportsReturnTypeProcessor> SUPPORTS_RETURN_TYPE_PROCESSORS = new CopyOnWriteArrayList<>();

    /**
     * 【失败】全局响应环绕处理器列表
     */
    private static final List<BaseFailAroundProcessor> FAIL_AROUND_PROCESSORS = new CopyOnWriteArrayList<>();

    /**
     * 【失败】全局响应处理器列表
     */
    @SuppressWarnings({"rawtypes"})
    private static final List<BaseFailProcessor> FAIL_PROCESSORS = new CopyOnWriteArrayList<>();

    /**
     * 【成功】全局响应环绕处理器列表
     */
    private static final List<BaseSuccessAroundProcessor> SUCCESS_AROUND_PROCESSORS = new CopyOnWriteArrayList<>();

    /**
     * 【成功】全局响应处理器列表
     */
    private static final List<BaseSuccessProcessor> SUCCESS_PROCESSORS = new CopyOnWriteArrayList<>();

    /**
     * 添加所有的异常环绕处理器列表、异常处理器列表
     */
    @PostConstruct
    public void syncProcessors() {
        ApplicationContext applicationContext = SpringUtils.getApplicationContext();

        SUPPORTS_RETURN_TYPE_PROCESSORS.addAll(applicationContext.getBeansOfType(BaseSupportsReturnTypeProcessor.class).values());

        SUCCESS_AROUND_PROCESSORS.addAll(applicationContext.getBeansOfType(BaseSuccessAroundProcessor.class).values());
        SUCCESS_AROUND_PROCESSORS.sort(Comparator.comparing(BaseSuccessAroundProcessor::order));
        SUCCESS_PROCESSORS.addAll(applicationContext.getBeansOfType(BaseSuccessProcessor.class).values());

        FAIL_AROUND_PROCESSORS.addAll(applicationContext.getBeansOfType(BaseFailAroundProcessor.class).values());
        FAIL_AROUND_PROCESSORS.sort(Comparator.comparing(BaseFailAroundProcessor::order));
        FAIL_PROCESSORS.addAll(applicationContext.getBeansOfType(BaseFailProcessor.class).values());
    }

    /**
     * 是否执行全局响应
     * <p>
     * 可自定义一些请求进行放行
     * <p>
     * 返回 true 则为放行
     *
     * @param returnType returnType
     * @return 是否执行全局响应
     */
    @Override
    public boolean supportsReturnType(@NonNull MethodParameter returnType) {
        for (BaseSupportsReturnTypeProcessor processor : SUPPORTS_RETURN_TYPE_PROCESSORS) {
            boolean result = processor.supportsReturnType(returnType);
            if (result) {
                return false;
            }
        }
        return super.supportsReturnType(returnType);
    }

    /**
     * 统一处理响应数据体
     * <p>
     * 包含特殊业务请求、成功请求、失败请求
     * <p>
     * 在 {@link RequestResponseBodyMethodProcessor#handleReturnValue(java.lang.Object, org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest)} 执行业务处理前，替换 returnValue 值，替换完后再接着走对应的业务处理
     * <p>
     * 不使用 implements {@link org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice#beforeBodyWrite(java.lang.Object, org.springframework.core.MethodParameter, org.springframework.http.MediaType, java.lang.Class, org.springframework.http.server.ServerHttpRequest, org.springframework.http.server.ServerHttpResponse)} 方式去实现替换值的原因如下：
     * <p>
     * 在执行 {@link org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice#beforeBodyWrite(java.lang.Object, org.springframework.core.MethodParameter, org.springframework.http.MediaType, java.lang.Class, org.springframework.http.server.ServerHttpRequest, org.springframework.http.server.ServerHttpResponse)} 之前已经确认好 {@link org.springframework.http.MediaType}、{@link HttpMessageConverter} ，
     * 在上层逻辑无法进行动态的修改这两个参数（这块都是 Spring 提供的，要修改源码破坏性太大，以及有一个其他办法。那就是自定义 {@link HttpMessageConverter} ，但是自带的就能用，没必要专门写一个 99% 重复代码率的类，只差在范型以及对应接收的请求头类型参数上）
     * <p>
     * 目前理论上能拦截所有请求进行封装，但是有一个漏网之鱼
     * <p>
     * 那就是 {link org.springframework.boot.autoconfigure.web.WebProperties.Resources.addMappings} 参数设置为 {@link Boolean#TRUE} 时，请求不存在的接口时会报 404 Not Found。
     *
     * @param returnValue  returnValue
     * @param returnType   returnType
     * @param mavContainer mavContainer
     * @param webRequest   webRequest
     * @throws HttpMediaTypeNotAcceptableException HTTP 媒体类型不可接受异常
     * @throws IOException                         IO 异常
     */
    @Override
    public void handleReturnValue(Object returnValue,
                                  @NonNull MethodParameter returnType,
                                  @NonNull ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest) throws HttpMediaTypeNotAcceptableException, IOException {
        Object result = returnValue;
        Class<?> methodReturnType = Objects.requireNonNull(returnType.getMethod()).getReturnType();
        HttpServletRequest request = SpringUtils.getHttpServletRequest();
        try {
            if (result != null && result.getClass() == ResponseEntity.class || methodReturnType == ResponseEntity.class) {
                if (returnType.getContainingClass() == GlobalExceptionHandler.class) {
                    // 已在全局异常处理过了，直接返回
                    result = this.getResponseEntityBody(false, result);
                    return;
                } else if (returnType.getContainingClass() == BasicErrorController.class && request != null) {
                    // 是否是 404 Not Found
                    Throwable exception = new NoHandlerFoundException(request.getMethod(), request.getRequestURI(),
                            new ServletServerHttpRequest(request).getHeaders());
                    result = this.processFail(exception);
                    return;
                }
                // 其他 Web 资源信息
                result = this.getResponseEntityBody(true, result);
                return;
            }
            if (request != null) {
                Object exception = request.getAttribute(GlobalExceptionHandler.class.getName());
                if (exception != null) {
                    result = this.processFail((Throwable) exception);
                    return;
                }
            }
            result = this.processSuccess(result);
        } finally {
            super.handleReturnValue(result, returnType, mavContainer, webRequest);
        }
    }

    /**
     * 获取 responseEntity body 数据
     *
     * @param isSuccess      请求是否成功
     * @param responseEntity responseEntity
     * @return 获取 responseEntity body 数据
     */
    private Object getResponseEntityBody(boolean isSuccess, Object responseEntity) {
        Object body = ((ResponseEntity<?>) responseEntity).getBody();
        if (isSuccess) {
            try {
                log.info("SUCCESS RESPONSE result: [{}]", jsonMapper.writeValueAsString(Optional.ofNullable(body).orElse("")));
            } catch (JsonProcessingException e) {
                log.error(e.getLocalizedMessage());
                log.info("SUCCESS RESPONSE result: [{}]", Optional.ofNullable(body).orElse(""));
            }
        } else {
            try {
                log.error("FAIL RESPONSE result: [{}]", jsonMapper.writeValueAsString(Optional.ofNullable(body).orElse("")));
            } catch (JsonProcessingException e) {
                log.error(e.getLocalizedMessage());
                log.error("FAIL RESPONSE result: [{}]", Optional.ofNullable(body).orElse(""));
            }
        }
        return body;
    }

    /**
     * 处理失败请求
     *
     * @param exception 异常
     * @return 响应数据体
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object processFail(Throwable exception) {
        Object result = exception.getLocalizedMessage();
        boolean isHitAroundProcessor = false;
        // 首先执行环绕处理器
        for (BaseFailAroundProcessor aroundProcessor : FAIL_AROUND_PROCESSORS) {
            if (!aroundProcessor.isHitProcessor(exception)) {
                continue;
            }
            result = aroundProcessor.exec(exception);
            aroundProcessor.execAfter(exception);
            isHitAroundProcessor = true;
            break;
        }
        // 其次匹配处理器
        Optional<BaseFailProcessor> opProcessor = FAIL_PROCESSORS.stream()
                .filter(p -> p.isHitProcessor(exception))
                .max(Comparator.comparing(BaseFailProcessor::order));
        if (!isHitAroundProcessor && opProcessor.isPresent()) {
            BaseFailProcessor processor = opProcessor.get();
            result = processor.exec(exception);
            processor.log(exception);
            processor.execAfter(exception);
            HttpServletResponse response = SpringUtils.getHttpServletResponse();
            if (response != null) {
                response.setStatus(processor.httpStatus().value());
            }
        }
        try {
            log.error("FAIL RESPONSE result: [{}]", jsonMapper.writeValueAsString(Optional.ofNullable(result).orElse("")));
        } catch (JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
            log.error("FAIL RESPONSE result: [{}]", Optional.ofNullable(result).orElse(""));
        }
        return result;
    }

    /**
     * 处理成功请求
     *
     * @param body body
     * @return 响应数据体
     */
    private Object processSuccess(Object body) {
        Object result = body;
        boolean isHitAroundProcessor = false;
        // 首先执行环绕处理器
        for (BaseSuccessAroundProcessor aroundProcessor : SUCCESS_AROUND_PROCESSORS) {
            if (!aroundProcessor.isHitProcessor(body)) {
                continue;
            }
            result = aroundProcessor.exec(body);
            aroundProcessor.execAfter(result);
            isHitAroundProcessor = true;
            break;
        }
        // 其次匹配处理器
        Optional<BaseSuccessProcessor> opProcessor = SUCCESS_PROCESSORS.stream()
                .filter(BaseSuccessProcessor::isHitProcessor)
                .max(Comparator.comparing(BaseSuccessProcessor::order));
        if (!isHitAroundProcessor && opProcessor.isPresent()) {
            BaseSuccessProcessor processor = opProcessor.get();
            result = processor.exec(body);
            processor.execAfter(result);
            HttpServletResponse response = SpringUtils.getHttpServletResponse();
            if (response != null) {
                response.setStatus(processor.httpStatus().value());
            }
        }
        try {
            log.info("SUCCESS RESPONSE result: [{}]", jsonMapper.writeValueAsString(Optional.ofNullable(result).orElse("")));
        } catch (JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
            log.info("SUCCESS RESPONSE result: [{}]", Optional.ofNullable(result).orElse(""));
        }
        return result;
    }

}