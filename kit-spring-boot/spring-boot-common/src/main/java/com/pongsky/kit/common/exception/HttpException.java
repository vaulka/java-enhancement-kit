package com.pongsky.kit.common.exception;

/**
 * HTTP 请求异常
 *
 * @author pengsenhao
 */
public class HttpException extends RuntimeException {

    private static final long serialVersionUID = -5701953556548839172L;

    public HttpException(String message, Exception e) {
        super(message, e);
    }

}