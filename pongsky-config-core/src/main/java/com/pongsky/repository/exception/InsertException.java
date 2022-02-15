package com.pongsky.repository.exception;

/**
 * 保存异常
 *
 * @author pengsenhao
 * @create 2021-02-12
 */
public class InsertException extends RuntimeException {

    private static final long serialVersionUID = -3937788835135997513L;

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
