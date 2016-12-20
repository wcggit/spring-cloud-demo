package com.jfk.common.event.mapper;

import com.jfk.base.mapper.MyMapper;
import com.jfk.common.event.domain.RevokeAskEventPublish;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by wcg on 2016/12/7.
 */
public interface RevokeAskEventPublishMapper extends MyMapper<RevokeAskEventPublish> {

  @Select(value = "select * from revoke_ask_event_publish where status = #{status}")
  List<RevokeAskEventPublish> findByStatus(@Param("status")String status);
}
