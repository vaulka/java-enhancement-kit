package com.pongsky.kit.desensitization.utils;

/**
 * @author pengsenhao
 * @date 2022-03-15 11:49
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
