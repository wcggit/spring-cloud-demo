package com.jfk.common.event.domain;


/**
 * Created by liubin on 2016/3/28.
 */
public class RevokeAskEventPublish extends EventPublish {

    private Long askEventId;

    public Long getAskEventId() {
        return askEventId;
    }

    public void setAskEventId(Long askEventId) {
        this.askEventId = askEventId;
    }
}
