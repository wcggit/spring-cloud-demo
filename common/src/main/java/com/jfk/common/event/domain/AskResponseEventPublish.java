package com.jfk.common.event.domain;


import org.apache.ibatis.type.JdbcType;

import tk.mybatis.mapper.annotation.ColumnType;

/**
 * Created by liubin on 2016/3/28.
 */
public class AskResponseEventPublish extends EventPublish {

    @ColumnType(jdbcType = JdbcType.BOOLEAN)
    private boolean success;

    private Long askEventId;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getAskEventId() {
        return askEventId;
    }

    public void setAskEventId(Long askEventId) {
        this.askEventId = askEventId;
    }
}
