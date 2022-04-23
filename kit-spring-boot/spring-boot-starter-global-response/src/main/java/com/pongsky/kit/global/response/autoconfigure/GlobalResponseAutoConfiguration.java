package com.pongsky.kit.global.response.autoconfigure;

import com.pongsky.kit.global.response.config.GlobalResponseWebMvcConfigurer;
import com.pongsky.kit.global.response.handler.GlobalExceptionHandler;
import com.pongsky.kit.global.response.handler.GlobalResponseHandler;
import com.pongsky.kit.global.response.handler.processor.fail.impl.AccessDeniedExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.BindExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.CircuitBreakerExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.DefaultFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.DeleteExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.DoesNotExistExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.ExistExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.FrequencyExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.HttpExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.HttpMessageNotReadableExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.HttpRequestMethodNotSupportedExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.IllegalArgumentExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.InsertExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.MaxUploadSizeExceededExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.MissingServletRequestParameterExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.MultipartExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.NoHandlerFoundExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.NullPointerExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.RuntimeExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.TypeMismatchExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.UpdateExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.fail.impl.ValidationExceptionFailProcessor;
import com.pongsky.kit.global.response.handler.processor.success.impl.DefaultSuccessProcessor;
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
        GlobalExceptionHandler.class, GlobalResponseHandler.class,
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
        MissingServletRequestParameterExceptionFailProcessor.class, TypeMismatchExceptionFailProcessor.class
})
public class GlobalResponseAutoConfiguration {


}
