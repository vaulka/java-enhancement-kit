package com.pongsky.repository.exception;

/**
 * 存在异常
 *
 * @author pengsenhao
 * @create 2021-02-11
 */
public class ExistException extends RuntimeException {

    private static final long serialVersionUID = 5804665095493997255L;

    public ExistException(String message) {
        super(message);
    }

}
