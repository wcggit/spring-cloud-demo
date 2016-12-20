package com.jfk.base.event.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jfk.base.event.constants.FailureInfo;
import com.jfk.base.event.constants.EventType;

/**
 * Created by liubin on 2016/6/3.
 */
public final class RevokeAskEvent extends BaseEvent {

    public static final String EVENT_TYPE = EventType.REVOKE_ASK.name();



    private FailureInfo failureInfo;

    private Long askEventId;

    public RevokeAskEvent(
            @JsonProperty("failureInfo") FailureInfo failureInfo,
            @JsonProperty("askEventId") Long askEventId) {
        this.failureInfo = failureInfo;
        this.askEventId = askEventId;
    }

    public FailureInfo getFailureInfo() {
        return failureInfo;
    }

    public Long getAskEventId() {
        return askEventId;
    }

    @Override
    public String toString() {
        return "RevokeAskEvent{" +
                "failureInfo=" + failureInfo +
                ", askEventId=" + askEventId +
                "} " + super.toString();
    }

    @Override
    public String getType() {
        return EVENT_TYPE;
    }
}
