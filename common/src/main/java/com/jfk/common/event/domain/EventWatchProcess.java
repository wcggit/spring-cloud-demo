package com.jfk.common.event.domain;


import com.jfk.common.domain.AuditEntity;
import com.jfk.common.event.constant.ProcessStatus;

import org.apache.ibatis.type.JdbcType;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import tk.mybatis.mapper.annotation.ColumnType;

/**
 * Created by liubin on 2016/3/28.
 */
public class EventWatchProcess extends AuditEntity {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String failureInfo;

    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String status = ProcessStatus.NEW.name();

    private Long watchId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFailureInfo() {
        return failureInfo;
    }

    public void setFailureInfo(String failureInfo) {
        this.failureInfo = failureInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getWatchId() {
        return watchId;
    }

    public void setWatchId(Long watchId) {
        this.watchId = watchId;
    }


    @Override
    public String toString() {
        return "EventWatchProcess{" +
                "watchId=" + watchId +
                ", status=" + status +
                ", failureInfo='" + failureInfo + '\'' +
                ", id=" + id +
                "} " + super.toString();
    }
}
