package com.pongsky.repository.utils.desensitization;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-02-08 3:21 下午
 */
@Getter
@AllArgsConstructor
public enum DesensitizationType {

    /**
     * 名称脱敏
     * <p>
     * 示例：彭*
     * 示例：彭*豪
     * 示例：彭**豪
     */
    NAME("名称脱敏") {
        @Override
        public String desensitization(String str) {
            return str.length() == 2
                    ? DesensitizationUtils.desensitization(str, 1, 0)
                    : DesensitizationUtils.desensitization(str, 1, 1);
        }
    },

    /**
     * 手机号脱敏
     * <p>
     * 示例：151***5510
     */
    PHONE("手机号脱敏") {
        @Override
        public String desensitization(String str) {
            return DesensitizationUtils.desensitization(str, 3, 4);
        }
    },

    /**
     * 身份证脱敏
     * <p>
     * 示例：350***********2510
     */
    ID_CARD("身份证脱敏") {
        @Override
        public String desensitization(String str) {
            return DesensitizationUtils.desensitization(str, 3, 4);
        }
    },

    /**
     * 邮箱脱敏
     * <p>
     * 示例：k****@vip.qq.com
     */
    EMAIL("邮箱脱敏") {
        @Override
        public String desensitization(String str) {
            return DesensitizationUtils.desensitization(str, 1, str.lastIndexOf("@"));
        }
    },

    /**
     * 银行卡号脱敏
     * <p>
     * 示例：6217********1201
     */
    BANK_CARD("银行卡号脱敏") {
        @Override
        public String desensitization(String str) {
            return DesensitizationUtils.desensitization(str, 4, 4);
        }
    },

    /**
     * 密码脱敏
     * <p>
     * 示例：******
     */
    PASSWORD("密码脱敏") {
        @Override
        public String desensitization(String str) {
            return DesensitizationUtils.desensitization(str, 0, 0);
        }
    },

    ;

    /**
     * 脱敏类型
     */
    private final String type;

    /**
     * 数据脱敏
     *
     * @param str 明文信息
     * @return 脱敏后的信息
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-02-08 3:22 下午
     */
    public abstract String desensitization(String str);

}
