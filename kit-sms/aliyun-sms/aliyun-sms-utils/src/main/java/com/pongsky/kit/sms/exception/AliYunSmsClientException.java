package com.pongsky.kit.sms.exception;

/**
 * 阿里云短信连接异常
 *
 * @author pengsenhao
 */
public class AliYunSmsClientException extends RuntimeException {

    private static final long serialVersionUID = 2123039075370982639L;

    public AliYunSmsClientException(String message) {
        super(message);
    }

    public AliYunSmsClientException(String message, Exception e) {
        super(message, e);
    }

}
