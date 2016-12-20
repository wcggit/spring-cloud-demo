package com.jfk.common.event.mapper.provider;

import java.util.List;
import java.util.Map;

/**
 * Created by wcg on 2016/12/8.
 */
public class AskRequestEventPublishProvider {

  public static String findAllByEventIdIn(Map params) {
    StringBuffer sql = new StringBuffer();
    sql.append("select * from ask_request_event_publish  where event_id in (");
    List<Long> ids = (List<Long>) params.get("ids");
    for (int i = 0; i < ids.size(); i++) {
      if (i + 1 == ids.size()) {
        sql.append(ids.get(i));
      } else {
        sql.append(ids.get(i));
        sql.append(",");
      }
    }
    sql.append(")");
    return sql.toString();
  }

}
