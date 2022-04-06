package com.pongsky.kit.desensitization.handler;

/**
 * 脱敏处理器
 *
 * @author pengsenhao
 */
public interface DesensitizationHandler {

    /**
     * 执行数据脱敏
     *
     * @param str 明文信息
     * @return 脱敏后的信息
     * @author pengsenhao
     */
    String exec(String str);

}
