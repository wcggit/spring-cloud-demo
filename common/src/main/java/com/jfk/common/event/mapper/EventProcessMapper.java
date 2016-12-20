package com.jfk.common.event.mapper;

import com.jfk.base.mapper.MyMapper;
import com.jfk.common.event.domain.EventProcess;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by wcg on 2016/12/7.
 */
public interface EventProcessMapper extends MyMapper<EventProcess> {

  @Select(value = "select * from event_process where status = #{status}"
  )
  List<EventProcess> findByStatus(@Param("status") String status);

  @Select(value = "select * from event_process where event_id = #{askEventId}"
  )
  EventProcess selectByEventId(@Param("askEventId") Long askEventId);

  @Select(value = "select * from event_process where event_id = #{eventId} and event_type = #{eventType} and group_name = #{groupName}"
  )
  EventProcess selectByEventIdAndEventTypeAndConsumerGroup(@Param("eventId") Long eventId,
                                                           @Param("eventType") String name,
                                                           @Param("groupName") String groupName);
}
