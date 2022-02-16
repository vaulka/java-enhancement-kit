package com.pongsky.springcloud.exception;

/**
 * 频率异常
 *
 * @author pengsenhao
 */
public class FrequencyException extends RuntimeException {

    private static final long serialVersionUID = -954678963872186012L;

    public FrequencyException(String message) {
        super(message);
    }

}
