package com.pongsky.kit.sms.exception;

/**
 * 腾讯云短信异常
 *
 * @author pengsenhao
 */
public class TenCentSmsException extends RuntimeException {

    private static final long serialVersionUID = 84357496196730935L;

    public TenCentSmsException(String message) {
        super(message);
    }

    public TenCentSmsException(String message, Exception e) {
        super(message, e);
    }

}
