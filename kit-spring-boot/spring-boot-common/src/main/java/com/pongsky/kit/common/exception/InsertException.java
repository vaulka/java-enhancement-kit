package com.pongsky.kit.common.exception;

/**
 * 保存异常
 *
 * @author pengsenhao
 */
public class InsertException extends RuntimeException {

    public InsertException(String message) {
        super(message);
    }

    /**
     * 校验保存 SQL
     *
     * @param exceptionMessage 异常信息
     * @param result           结果
     */
    public static void validation(String exceptionMessage, Boolean result) {
        if (!result) {
            throw new InsertException(exceptionMessage);
        }
    }

    /**
     * 校验保存 SQL
     *
     * @param exceptionMessage 异常信息
     * @param insertCount      保存总数
     */
    public static void validation(String exceptionMessage, Integer insertCount) {
        validation(exceptionMessage, insertCount, 1);
    }

    /**
     * 校验保存 SQL
     *
     * @param exceptionMessage 异常信息
     * @param insertCount      保存总数
     * @param dataCount        数据总数
     */
    public static void validation(String exceptionMessage, Integer insertCount, Integer dataCount) {
        if (!insertCount.equals(dataCount)) {
            throw new InsertException(exceptionMessage);
        }
    }

}
