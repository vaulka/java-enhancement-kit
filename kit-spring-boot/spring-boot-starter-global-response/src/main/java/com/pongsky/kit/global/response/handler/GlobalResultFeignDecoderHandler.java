package com.pongsky.kit.global.response.handler;

import com.pongsky.kit.common.response.GlobalResult;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Feign 远程调用校验解析
 *
 * @author pengsenhao
 */
@ConditionalOnClass({Decoder.class, SpringDecoder.class})
public class GlobalResultFeignDecoderHandler {

    /**
     * 封装全局响应体校验解析器
     *
     * @param messageConverters messageConverters
     * @return 封装全局响应体校验解析器
     */
    @Bean
    public Decoder feignDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new GlobalResultDecoder(new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters))));
    }

    @RequiredArgsConstructor
    public static class GlobalResultDecoder implements Decoder {

        private final Decoder decoder;

        @Override
        public Object decode(Response response, Type type) throws IOException, FeignException {
            Object decode = decoder.decode(response, type);
            if (decode instanceof GlobalResult) {
                GlobalResult<?> result = (GlobalResult<?>) decode;
                // 如果 code 不是成功状态码，则直接抛出异常
                result.feignValidation();
            }
            return decode;
        }
    }

}
