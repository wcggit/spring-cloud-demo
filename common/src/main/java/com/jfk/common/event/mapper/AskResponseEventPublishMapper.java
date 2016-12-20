package com.jfk.common.event.mapper;

import com.jfk.base.mapper.MyMapper;
import com.jfk.common.event.domain.AskResponseEventPublish;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by wcg on 2016/12/7.
 */
public interface AskResponseEventPublishMapper extends MyMapper<AskResponseEventPublish> {

  @Select(value = "select count(*) from ask_response_event_publish where ask_event_id = #{askEventId}")
  long countByAskEventId(@Param("askEventId") Long askEventId);

  @Select(value = "select * from ask_response_event_publish where status = #{status}")
  List<AskResponseEventPublish> findByStatus(@Param("status") String status);

  @Select(value = "select success from ask_response_event_publish where ask_event_id = #{eventId}")
  Boolean findByAskEventId(Long eventId);
}
