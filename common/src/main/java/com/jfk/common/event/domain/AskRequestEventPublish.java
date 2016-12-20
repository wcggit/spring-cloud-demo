package com.jfk.common.event.domain;


import com.jfk.common.event.constant.AskEventStatus;

import org.apache.ibatis.type.JdbcType;

import tk.mybatis.mapper.annotation.ColumnType;

/**
 * Created by liubin on 2016/3/28.
 */
public class AskRequestEventPublish extends EventPublish {

    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String askEventStatus;

    private Long watchId;


    public String getAskEventStatus() {
        return askEventStatus;
    }

    public void setAskEventStatus(String askEventStatus) {
        this.askEventStatus = askEventStatus;
    }

    public Long getWatchId() {
        return watchId;
    }

    public void setWatchId(Long watchId) {
        this.watchId = watchId;
    }

}
