package com.pongsky.kit.sms.exception;

/**
 * 阿里云短信业务异常
 *
 * @author pengsenhao
 */
public class AliyunSmsBizException extends RuntimeException {

    private static final long serialVersionUID = -8619878126315797853L;

    public AliyunSmsBizException(String message) {
        super(message);
    }

    public AliyunSmsBizException(String message, Exception e) {
        super(message, e);
    }

}
