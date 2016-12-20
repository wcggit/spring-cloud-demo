package com.jfk.common.event.mapper;

import com.jfk.base.mapper.MyMapper;
import com.jfk.common.event.domain.EventWatch;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by wcg on 2016/12/7.
 */
public interface EventWatchMapper extends MyMapper<EventWatch> {

  @Select(value = "select * from event_watch where ask_event_status = #{status} and timeout_time < #{timeoutTime}")
  List<EventWatch> findByAskEventStatusAndTimeoutTimeBefore(@Param("status") String status,
                                                            @Param("timeoutTime") Date timeoutTime);
}
