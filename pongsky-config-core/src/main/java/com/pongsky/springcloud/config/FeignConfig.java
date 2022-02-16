package com.pongsky.springcloud.config;

import com.pongsky.springcloud.response.GlobalResult;
import com.pongsky.springcloud.utils.trace.CurrentThreadConfig;
import com.pongsky.springcloud.utils.trace.DiyHeader;
import feign.FeignException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Feign 配置
 *
 * @author pengsenhao
 */
@Component
public class FeignConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 重新设置 user-agent，原始 user-agent 是 jdk 版本，会变化
        // 限流逻辑会判断 user-agent 是不是 远程调用，是的话则忽略请求限流
        requestTemplate.header(HttpHeaders.USER_AGENT, DiyHeader.REMOTE_PREFIX);
        // 将原接口的鉴权信息传递到远程调用接口
        requestTemplate.header(HttpHeaders.AUTHORIZATION, CurrentThreadConfig.getAuthorization());
        String traceId = CurrentThreadConfig.getTraceId();
        if (StringUtils.isNotBlank(traceId)) {
            requestTemplate.header(DiyHeader.X_TRACE_ID, traceId.contains(DiyHeader.REMOTE_PREFIX)
                    ? traceId
                    : traceId.replace(DiyHeader.ORIGIN_PREFIX, DiyHeader.REMOTE_PREFIX));
        }
    }

    @Bean
    public Decoder feignDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new MyDecoder(new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters))));
    }

    public static class MyDecoder implements Decoder {

        private final Decoder decoder;

        public MyDecoder(Decoder decoder) {
            this.decoder = decoder;
        }

        @Override
        public Object decode(Response response, Type type) throws IOException, FeignException {
            Object decode = decoder.decode(response, type);
            if (decode instanceof GlobalResult) {
                GlobalResult<?> result = (GlobalResult<?>) decode;
                result.feignValidation();
            }
            return decode;
        }
    }

}