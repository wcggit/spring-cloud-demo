package com.jfk.common.exception;


import com.jfk.base.exception.BaseException;

/**
 * Created by liubin on 2016/4/14.
 */
public class EventException extends BaseException {

    public EventException(String message) {
        super(message);
    }

    public EventException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventException(Throwable cause) {
        super(cause);
    }
}
