package com.pongsky.kit.global.response.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pongsky.kit.core.utils.SpringUtils;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailAroundProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import com.pongsky.kit.global.response.handler.processor.success.BaseSuccessAroundProcessor;
import com.pongsky.kit.global.response.handler.processor.success.BaseSuccessProcessor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Spring MVC 全局响应处理器
 * <p>
 * code 标识如下：
 * <p>
 * 0：接口请求成功， > 0 接口请求失败
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
@RequiredArgsConstructor
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper jsonMapper;

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
     * 应用上下文
     */
    private static ApplicationContext APPLICATION_CONTEXT = null;

    /**
     * 添加所有的异常环绕处理器列表、异常处理器列表
     */
    @PostConstruct
    public void syncProcessors() {
        APPLICATION_CONTEXT = SpringUtils.getApplicationContext();

        SUCCESS_AROUND_PROCESSORS.addAll(APPLICATION_CONTEXT.getBeansOfType(BaseSuccessAroundProcessor.class).values());
        SUCCESS_PROCESSORS.addAll(APPLICATION_CONTEXT.getBeansOfType(BaseSuccessProcessor.class).values());

        FAIL_AROUND_PROCESSORS.addAll(APPLICATION_CONTEXT.getBeansOfType(BaseFailAroundProcessor.class).values());
        FAIL_PROCESSORS.addAll(APPLICATION_CONTEXT.getBeansOfType(BaseFailProcessor.class).values());
    }

    /**
     * 判断请求是否拦截处理
     *
     * @param returnType    returnType
     * @param converterType converterType
     * @return 判断请求是否拦截处理
     */
    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // 默认拦截所有请求，后续考虑放行一些系统/框架内置接口
        return true;
    }

    /**
     * 统一处理响应数据体
     * <p>
     * 包含特殊业务请求、成功请求、失败请求
     * <p>
     * {@link org.springframework.boot.autoconfigure.web.WebProperties.Resources.addMappings} 参数设置为 {@link Boolean#TRUE} 时
     * <p>
     * 就不会走 {@link ResponseEntityExceptionHandler#handleNoHandlerFoundException(org.springframework.web.servlet.NoHandlerFoundException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)} 异常处理方法，统一该异常响应数据格式需要再额外判断下
     *
     * @param body                  body
     * @param returnType            returnType
     * @param selectedContentType   selectedContentType
     * @param selectedConverterType selectedConverterType
     * @param request               request
     * @param response              response
     * @return 响应数据体
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = attributes.getRequest();
        HttpServletResponse httpServletResponse = attributes.getResponse();
        Class<?> methodReturnType = Objects.requireNonNull(returnType.getMethod()).getReturnType();
        if (methodReturnType == ResponseEntity.class && returnType.getContainingClass() == GlobalExceptionHandler.class) {
            // 已在全局异常处理过了，直接返回
            try {
                log.error("FAIL RESPONSE result: [{}]", jsonMapper.writeValueAsString(Optional.ofNullable(body).orElse("")));
            } catch (JsonProcessingException e) {
                log.error(e.getLocalizedMessage());
                log.error("FAIL RESPONSE result: [{}]", Optional.ofNullable(body).orElse(""));
            }
            return body;
        }
        Object exception;
        if (methodReturnType == ResponseEntity.class && returnType.getContainingClass() == BasicErrorController.class) {
            exception = new NoHandlerFoundException(httpServletRequest.getMethod(), httpServletRequest.getRequestURI(),
                    new ServletServerHttpRequest(httpServletRequest).getHeaders());
        } else {
            exception = httpServletRequest.getAttribute(GlobalExceptionHandler.class.getName());
        }
        Object result;
        if (exception != null) {
            result = this.processFail((Throwable) exception, httpServletRequest, httpServletResponse);
        } else {
            result = this.processSuccess(body, httpServletRequest, httpServletResponse);
        }
        if (methodReturnType == String.class && result.getClass() != String.class) {
            // Jackson 序列化 String 时，会使用 StringHttpMessageConverter 来处理返回体
            // 原本数据是 String 数据类型，响应体经过业务处理/封装后不是 String 数据类型时，直接返回会导致类型转换异常
            try {
                return jsonMapper.writeValueAsString(result);
            } catch (JsonProcessingException ignored) {
            }
        }
        return result;
    }

    /**
     * 处理失败请求
     *
     * @param exception 异常
     * @param request   request
     * @param response  response
     * @return 响应数据体
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object processFail(Throwable exception, HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        // 首先执行环绕处理器
        for (BaseFailAroundProcessor aroundProcessor : FAIL_AROUND_PROCESSORS) {
            result = aroundProcessor.execBefore(exception, request, APPLICATION_CONTEXT);
            if (result != null) {
                aroundProcessor.execAfter(exception, request, APPLICATION_CONTEXT);
                break;
            }
        }
        // 其次匹配处理器
        Optional<BaseFailProcessor> opProcessor = FAIL_PROCESSORS.stream()
                .filter(p -> p.isHitProcessor(exception, request, APPLICATION_CONTEXT))
                .findFirst();
        if (opProcessor.isPresent()) {
            BaseFailProcessor processor = opProcessor.get();
            if (result == null) {
                result = processor.exec(exception, request, APPLICATION_CONTEXT);
            }
            processor.log(exception, request, APPLICATION_CONTEXT);
            processor.execAfter(exception, request, APPLICATION_CONTEXT);
            if (response != null) {
                response.setStatus(processor.httpStatus().value());
            }
        }
        result = result != null ? result : exception.getLocalizedMessage();
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
     * @param body     body
     * @param request  request
     * @param response response
     * @return 响应数据体
     */
    private Object processSuccess(Object body, HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        // 首先执行环绕处理器
        for (BaseSuccessAroundProcessor aroundProcessor : SUCCESS_AROUND_PROCESSORS) {
            result = aroundProcessor.execBefore(body, request, APPLICATION_CONTEXT);
            if (result != null) {
                aroundProcessor.execAfter(result, request, APPLICATION_CONTEXT);
                break;
            }
        }
        // 其次匹配处理器
        Optional<BaseSuccessProcessor> opProcessor = SUCCESS_PROCESSORS.stream()
                .filter(p -> p.isHitProcessor(request, APPLICATION_CONTEXT))
                .findFirst();
        if (opProcessor.isPresent()) {
            BaseSuccessProcessor processor = opProcessor.get();
            if (result == null) {
                result = processor.exec(body, request, APPLICATION_CONTEXT);
            }
            processor.execAfter(request, request, APPLICATION_CONTEXT);
            if (response != null) {
                response.setStatus(processor.httpStatus().value());
            }
        }
        result = result != null ? result : body;
        try {
            log.info("SUCCESS RESPONSE result: [{}]", jsonMapper.writeValueAsString(Optional.ofNullable(result).orElse("")));
        } catch (JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
            log.info("SUCCESS RESPONSE result: [{}]", Optional.ofNullable(result).orElse(""));
        }
        return result;
    }

}