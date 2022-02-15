package com.pongsky.repository.exception;

/**
 * 更新异常
 *
 * @author pengsenhao
 * @create 2021-02-12
 */
public class UpdateException extends RuntimeException {

    private static final long serialVersionUID = -6162434953873081531L;

    public UpdateException(String message) {
        super(message);
    }

    /**
     * 校验更新 SQL
     *
     * @param exceptionMessage 异常信息
     * @param result           结果
     */
    public static void validation(String exceptionMessage, Boolean result) {
        if (!result) {
            throw new UpdateException(exceptionMessage);
        }
    }

    /**
     * 校验更新 SQL
     *
     * @param exceptionMessage 异常信息
     * @param updateCount      更新总数
     */
    public static void validation(String exceptionMessage, Integer updateCount) {
        validation(exceptionMessage, updateCount, 1);
    }

    /**
     * 校验更新 SQL
     *
     * @param exceptionMessage 异常信息
     * @param updateCount      更新总数
     * @param dataCount        数据总数
     */
    public static void validation(String exceptionMessage, Integer updateCount, Integer dataCount) {
        if (!updateCount.equals(dataCount)) {
            throw new UpdateException(exceptionMessage);
        }
    }

}
