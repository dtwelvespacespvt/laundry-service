package com.stanzaliving.laundry.exception;

public class NoRecordException extends RuntimeException{

    private static final long serialVersionUID = -3368655266237942363L;

    public NoRecordException(String message) {
        super(message);
    }

    public NoRecordException(Throwable cause) {
        super(cause);
    }

    public NoRecordException(String message, Throwable cause) {
        super(message, cause);
    }
}
