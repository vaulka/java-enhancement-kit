package com.pongsky.repository.exception;

import com.pongsky.repository.response.GlobalResult;
import feign.FeignException;
import org.springframework.http.HttpStatus;

/**
 * 远程调用异常
 *
 * @author pengsenhao
 * @create 2021-02-14
 */
public class RemoteCallException extends FeignException {

    private static final long serialVersionUID = 7642014467846068866L;

    private final GlobalResult<?> result;

    public GlobalResult<?> getResult() {
        return result;
    }

    public RemoteCallException(GlobalResult<?> result) {
        super(HttpStatus.OK.value(), result.getMessage());
        this.result = result;
    }

}
