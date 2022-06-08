package com.pongsky.kit.storage.exception;

/**
 * MinIO 异常
 *
 * @author pengsenhao
 */
public class MinIoException extends RuntimeException {

    private static final long serialVersionUID = -8939776616730992066L;

    public MinIoException(String message) {
        super(message);
    }

    public MinIoException(String message, Exception e) {
        super(message, e);
    }

}
