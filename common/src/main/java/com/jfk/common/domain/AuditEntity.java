package com.jfk.common.domain;


import org.apache.ibatis.type.JdbcType;

import java.util.Date;

import tk.mybatis.mapper.annotation.ColumnType;


/**
 * Created by liubin on 2016/3/28.
 */
public abstract class AuditEntity {

  @ColumnType(jdbcType = JdbcType.DATETIMEOFFSET)
  private Date createTime = new Date();

  @ColumnType(jdbcType = JdbcType.DATETIMEOFFSET)
  private Date updateTime = new Date();

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }
}
