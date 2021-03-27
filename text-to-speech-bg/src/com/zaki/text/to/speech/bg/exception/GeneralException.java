package com.zaki.text.to.speech.bg.exception;

public class GeneralException extends RuntimeException {
    public GeneralException(Throwable t) {
        super(t);
    }

    public GeneralException(String msg) {
        super(msg);
    }
}
