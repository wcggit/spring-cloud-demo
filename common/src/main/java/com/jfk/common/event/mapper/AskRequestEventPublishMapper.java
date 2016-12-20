package com.jfk.common.event.mapper;

import com.jfk.base.mapper.MyMapper;
import com.jfk.common.event.domain.AskRequestEventPublish;
import com.jfk.common.event.mapper.provider.AskRequestEventPublishProvider;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by wcg on 2016/12/7.
 */
public interface AskRequestEventPublishMapper extends MyMapper<AskRequestEventPublish> {

  @Select(value = "select * from ask_request_event_publish where event_id = #{askEventId}")
  AskRequestEventPublish getByEventId(@Param("askEventId")Long askEventId);

  @Select(value = "select * from ask_request_event_publish where status = #{status}")
  List<AskRequestEventPublish> findByStatus(@Param("status")String status);

  @SelectProvider(type = AskRequestEventPublishProvider.class,method = "findAllByEventIdIn")
  List<AskRequestEventPublish> findAllByEventIdIn(@Param("ids") List<Long> eventIds);
}
