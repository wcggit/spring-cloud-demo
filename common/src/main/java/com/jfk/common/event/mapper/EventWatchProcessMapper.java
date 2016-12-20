package com.jfk.common.event.mapper;

import com.jfk.base.mapper.MyMapper;
import com.jfk.common.event.domain.EventWatchProcess;
import com.jfk.common.event.mapper.provider.EventWatchProcessProvider;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by wcg on 2016/12/7.
 */
public interface EventWatchProcessMapper extends MyMapper<EventWatchProcess> {

  @Select(value = "select * from event_watch_process where status = #{status}")
  List<EventWatchProcess> findByStatus(@Param("status")String status);

  @SelectProvider(type = EventWatchProcessProvider.class, method = "updateStatusBatch")
  int updateStatusBatch(@Param("ids") Long[] ids, @Param("status") String status);
}
