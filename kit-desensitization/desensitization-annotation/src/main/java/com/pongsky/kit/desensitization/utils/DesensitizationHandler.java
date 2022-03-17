package com.pongsky.kit.desensitization.utils;

/**
 * 脱敏处理器
 *
 * @author pengsenhao
 */
public interface DesensitizationHandler {

    /**
     * 判断是否执行 {@link DesensitizationHandler#exec(java.lang.String)}
     *
     * @param str 明文信息
     * @return 判断是否执行结果
     * @author pengsenhao
     */
    boolean willDoExec(String str);

    /**
     * 执行数据脱敏
     *
     * @param str 明文信息
     * @return 脱敏后的信息
     * @author pengsenhao
     */
    String exec(String str);

}
