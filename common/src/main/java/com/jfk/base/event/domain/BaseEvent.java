package com.jfk.base.event.domain;

import java.util.Date;
import java.util.Objects;

/**
 * 子类必须定义static变量EVENT_TYPE
 * 例如: public static final EventType EVENT_TYPE = EventType.TEST_EVENT;
 * Created by liubin on 2016/4/8.
 */
public abstract class BaseEvent {

    protected Long id;

    protected Date createTime;

    public BaseEvent() {
        createTime = new Date();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public abstract String getType();



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEvent)) return false;
        BaseEvent baseEvent = (BaseEvent) o;
        return Objects.equals(id, baseEvent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BaseEvent{" +
               "id=" + id +
               ", createTime=" + createTime +
               '}';
    }
}
