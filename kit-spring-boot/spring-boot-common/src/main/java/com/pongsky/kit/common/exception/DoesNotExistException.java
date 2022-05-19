package com.pongsky.kit.common.exception;

/**
 * 不存在异常
 *
 * @author pengsenhao
 */
public class DoesNotExistException extends RuntimeException {

    private static final long serialVersionUID = -1875633352439939019L;

    public DoesNotExistException(String message) {
        super(message);
    }

}
