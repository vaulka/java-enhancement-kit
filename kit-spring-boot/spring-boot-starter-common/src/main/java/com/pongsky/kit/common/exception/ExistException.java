package com.pongsky.kit.common.exception;

/**
 * 存在异常
 *
 * @author pengsenhao
 */
public class ExistException extends RuntimeException {

    private static final long serialVersionUID = -4469874622807634893L;

    public ExistException(String message) {
        super(message);
    }

}
