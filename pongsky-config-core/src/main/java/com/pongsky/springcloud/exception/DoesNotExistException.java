package com.pongsky.springcloud.exception;

/**
 * 不存在异常
 *
 * @author pengsenhao
 * @create 2021-02-10
 */
public class DoesNotExistException extends RuntimeException {

    private static final long serialVersionUID = -7446575794897027038L;

    public DoesNotExistException(String message) {
        super(message);
    }

}
