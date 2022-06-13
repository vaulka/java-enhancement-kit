package com.pongsky.kit.sms.entity;

import java.util.List;

/**
 * 短信模版
 *
 * @author pengsenhao
 */
public abstract class SmsTemplate {

    /**
     * 短信模版变量
     */
    private final List<String> PARAM;

    public SmsTemplate(List<String> param) {
        this.PARAM = param;
    }

    /**
     * 短信模版 ID
     * <p>
     * 示例：1436600
     *
     * @return 短信模版 ID
     */
    public abstract String getId();

    /**
     * 短信模版格式，仅做留档用
     * <p>
     * 示例：{1} 为您的登录验证码，请于 {2} 分钟内填写。如非本人操作，请忽略本短信。
     *
     * @return 短信模版格式
     */
    public abstract String format();

    /**
     * 构建短信模版变量
     *
     * @return 短信模版变量
     */
    public final String[] buildParam() {
        return PARAM.toArray(new String[0]);
    }

}
