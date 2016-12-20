package com.jfk.common.event.mapper;

import com.jfk.base.mapper.MyMapper;
import com.jfk.common.event.domain.NotifyEventPublish;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by wcg on 2016/12/7.
 */
public interface NotifyEventPublishMapper extends MyMapper<NotifyEventPublish> {

  @Select(value = "select * from notify_event_publish where status = #{status}")
  List<NotifyEventPublish> findByStatus(@Param("status")String status);
}
