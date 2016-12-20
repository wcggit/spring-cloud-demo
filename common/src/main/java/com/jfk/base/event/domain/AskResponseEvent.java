package com.jfk.base.event.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jfk.base.event.constants.EventType;

/**
 * Created by liubin on 2016/6/3.
 */
public final class AskResponseEvent extends BaseEvent {

    public static final String EVENT_TYPE = EventType.ASK_RESPONSE.name();

    @Override
    public String getType() {
        return EVENT_TYPE;
    }

    private boolean success;

    private String message;

    private Long askEventId;

    public AskResponseEvent(
            @JsonProperty("success") boolean success,
            @JsonProperty("message") String message,
            @JsonProperty("askEventId") Long askEventId) {
        this.success = success;
        this.message = message;
        this.askEventId = askEventId;
    }

    public boolean isSuccess() {
        return success;
    }

    public Long getAskEventId() {
        return askEventId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "AskResponseEvent{" +
                "success=" + success +
                "message=" + message +
                ", askEventId=" + askEventId +
                "} " + super.toString();
    }
}
