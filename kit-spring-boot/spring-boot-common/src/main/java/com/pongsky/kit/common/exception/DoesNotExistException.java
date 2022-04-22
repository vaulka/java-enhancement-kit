package com.pongsky.kit.common.exception;

/**
 * 不存在异常
 *
 * @author pengsenhao
 */
public class DoesNotExistException extends RuntimeException {

    public DoesNotExistException(String message) {
        super(message);
    }

}
