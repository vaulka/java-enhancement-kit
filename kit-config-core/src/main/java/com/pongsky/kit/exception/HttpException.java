package com.pongsky.kit.exception;

/**
 * HTTP 请求异常
 *
 * @author pengsenhao
 */
public class HttpException extends RuntimeException {

    private static final long serialVersionUID = 746887887974658747L;

    public HttpException(String message, Exception e) {
        super(message, e);
    }

}