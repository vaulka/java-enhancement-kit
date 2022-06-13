package com.pongsky.kit.sms.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 短信模版
 *
 * @author pengsenhao
 */
public abstract class AliYunSmsTemplate<T> {

    /**
     * 短信模版变量
     */
    private final T PARAM;

    public AliYunSmsTemplate(T param) {
        this.PARAM = param;
    }

    /**
     * 短信模版 code
     * <p>
     * 示例：SMS_154950
     *
     * @return 短信模版 code
     */
    public abstract String getCode();

    /**
     * 短信模版格式，仅做留档用
     * <p>
     * 示例：您的验证码为 ${code} ，该验证码5分钟内有效，请勿泄露于他人。
     *
     * @return 短信模版格式
     */
    public abstract String format();

    /**
     * Jackson 序列化/反序列化工具
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 构建短信模版变量
     *
     * @return 短信模版变量
     */
    public String buildParam() {
        try {
            return MAPPER.writeValueAsString(PARAM);
        } catch (JsonProcessingException e) {
            return PARAM.toString();
        }
    }

}
