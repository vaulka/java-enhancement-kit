package com.pongsky.kit.exception;

/**
 * 存在异常
 *
 * @author pengsenhao
 */
public class ExistException extends RuntimeException {

    private static final long serialVersionUID = 5804665095493997255L;

    public ExistException(String message) {
        super(message);
    }

}
