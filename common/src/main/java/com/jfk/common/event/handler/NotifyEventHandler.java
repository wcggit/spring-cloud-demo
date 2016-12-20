package com.jfk.common.event.handler;


import com.jfk.base.event.domain.NotifyEvent;

/**
 * Created by liubin on 2016/6/3.
 */
public interface NotifyEventHandler<E extends NotifyEvent> {

    void notify(E event);

}
