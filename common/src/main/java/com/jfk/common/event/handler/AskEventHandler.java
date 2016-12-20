package com.jfk.common.event.handler;


import com.jfk.base.api.BooleanWrapper;
import com.jfk.base.event.domain.AskEvent;

/**
 * Created by liubin on 2016/6/3.
 */
public interface AskEventHandler<E extends AskEvent> {

    BooleanWrapper processRequest(E event);

}
