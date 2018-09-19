package com.wbq.spike.exception;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 16 九月 2018
 *  
 */
public class StateCodeException extends RuntimeException {
    private final int status;

    public StateCodeException(String message, int status) {
        super(message);
        this.status = status;
    }

    public StateCodeException(String message, Throwable cause, int status) {
        super(message, cause);
        this.status = status;
    }

    public StateCodeException(Throwable cause, int status) {
        super(cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
