package com.yunmall.ymsdk.utility.inject;


public class InjectException extends RuntimeException {

    public InjectException() {
        super();
    }

    public InjectException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InjectException(String detailMessage) {
        super(detailMessage);
    }

    public InjectException(Throwable throwable) {
        super(throwable);
    }

}
