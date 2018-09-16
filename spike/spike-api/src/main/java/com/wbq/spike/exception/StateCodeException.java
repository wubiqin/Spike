package com.wbq.spike.exception;

/**
 *  *
 *  * @author biqin.wu
 *  * @since 16 九月 2018
 *  
 */
public class StateCodeException extends RuntimeException {
    private final int status;

    private final String message;

    public StateCodeException(String message, int status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public StateCodeException(String message, Throwable cause, int status, String message1) {
        super(message, cause);
        this.status = status;
        this.message = message1;
    }

    public StateCodeException(Throwable cause, int status, String message) {
        super(cause);
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
