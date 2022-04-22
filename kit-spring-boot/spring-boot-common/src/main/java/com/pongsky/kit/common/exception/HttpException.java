package com.pongsky.kit.common.exception;

/**
 * HTTP 请求异常
 *
 * @author pengsenhao
 */
public class HttpException extends RuntimeException {

    public HttpException(String message, Exception e) {
        super(message, e);
    }

}