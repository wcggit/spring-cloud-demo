package com.jfk.common.domain;


/**
 * Created by liubin on 2016/3/28.
 */
public abstract class VersionEntity extends AuditEntity {

    private long version;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
