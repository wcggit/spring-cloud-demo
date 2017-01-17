package com.jfk.common.event.mapper.provider;

import java.util.List;
import java.util.Map;

/**
 * Created by wcg on 2016/12/8.
 */
public class EventWatchProcessProvider {

  public static String updateStatusBatch(Map params) {
    StringBuffer sql = new StringBuffer();
    Long[] ids = (Long[]) params.get("ids");
    String status = (String) params.get("status");
    sql.append("update event_watch_process set status = '");
    sql.append(status);
    sql.append("' where id in (");
    for (int i = 0; i < ids.length; i++) {
      if (i + 1 == ids.length) {
        sql.append(ids[i]);
      } else {
        sql.append(ids[i]);
        sql.append(",");
      }
    }
    sql.append(")");
    return sql.toString();
  }

}
