package com.pongsky.kit.sms.exception;

/**
 * 阿里云短信业务异常
 *
 * @author pengsenhao
 */
public class AliYunSmsBizException extends RuntimeException {

    private static final long serialVersionUID = -8619878126315797853L;

    public AliYunSmsBizException(String message) {
        super(message);
    }

    public AliYunSmsBizException(String message, Exception e) {
        super(message, e);
    }

}
