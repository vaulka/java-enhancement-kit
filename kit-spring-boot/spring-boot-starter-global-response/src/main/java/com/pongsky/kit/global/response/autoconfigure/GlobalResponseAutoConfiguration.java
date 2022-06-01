package com.pongsky.kit.global.response.autoconfigure;

import com.pongsky.kit.global.response.config.GlobalResponseWebMvcConfigurer;
import com.pongsky.kit.global.response.handler.GlobalExceptionHandler;
import com.pongsky.kit.global.response.handler.GlobalResponseHandler;
import com.pongsky.kit.global.response.handler.GlobalResultFeignDecoderHandler;
import com.pongsky.kit.global.response.handler.processor.fail.AccessDeniedExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.BindExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.CircuitBreakerExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.DefaultFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.DeleteExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.DoesNotExistExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.ExistExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.FrequencyExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.HttpExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.HttpMessageNotReadableExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.HttpRequestMethodNotSupportedExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.IllegalArgumentExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.InsertExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.MaxUploadSizeExceededExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.MissingServletRequestParameterExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.MultipartExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.NoHandlerFoundExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.NullPointerExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.RemoteCallExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.RuntimeExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.TypeMismatchExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.UpdateExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.ValidationExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.success.DefaultSuccessProcessor;
import com.pongsky.kit.global.response.handler.processor.supports.actuator.ActuatorReturnTypeProcessor;
import com.pongsky.kit.global.response.handler.processor.supports.springdoc.OpenApiWebMvcResourceSupportsReturnTypeProcessor;
import com.pongsky.kit.global.response.handler.processor.supports.springfox.ApiResourceControllerSupportsReturnTypeProcessor;
import com.pongsky.kit.global.response.handler.processor.supports.springfox.OpenApiControllerWebMvcSupportsReturnTypeProcessor;
import com.pongsky.kit.global.response.handler.processor.supports.springfox.Swagger2ControllerWebMvcSupportsReturnTypeProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 全局响应 自动装配
 *
 * @author pengsenhao
 **/
@Configuration(proxyBeanMethods = false)
@Import({
        GlobalResponseWebMvcConfigurer.class,
        GlobalExceptionHandler.class, GlobalResponseHandler.class, GlobalResultFeignDecoderHandler.class,
        // 成功请求处理器
        DefaultSuccessProcessor.class,
        // 失败请求处理器
        DefaultFailProcessor.class, AccessDeniedExceptionFailProcessor.class,
        CircuitBreakerExceptionFailProcessor.class, DeleteExceptionFailProcessor.class,
        DoesNotExistExceptionFailProcessor.class, ExistExceptionFailProcessor.class,
        FrequencyExceptionFailProcessor.class, HttpExceptionFailProcessor.class,
        IllegalArgumentExceptionFailProcessor.class, InsertExceptionFailProcessor.class,
        MaxUploadSizeExceededExceptionFailProcessor.class, MultipartExceptionFailProcessor.class,
        RuntimeExceptionFailProcessor.class, UpdateExceptionFailProcessor.class,
        ValidationExceptionFailProcessor.class, NullPointerExceptionFailProcessor.class,
        NoHandlerFoundExceptionFailProcessor.class, HttpRequestMethodNotSupportedExceptionFailProcessor.class,
        HttpMessageNotReadableExceptionFailProcessor.class, BindExceptionFailProcessor.class,
        MissingServletRequestParameterExceptionFailProcessor.class, TypeMismatchExceptionFailProcessor.class,
        RemoteCallExceptionFailProcessor.class,
        // 是否执行全局响应处理器
        ApiResourceControllerSupportsReturnTypeProcessor.class, OpenApiControllerWebMvcSupportsReturnTypeProcessor.class,
        Swagger2ControllerWebMvcSupportsReturnTypeProcessor.class, ActuatorReturnTypeProcessor.class,
        OpenApiWebMvcResourceSupportsReturnTypeProcessor.class
})
public class GlobalResponseAutoConfiguration {


}
