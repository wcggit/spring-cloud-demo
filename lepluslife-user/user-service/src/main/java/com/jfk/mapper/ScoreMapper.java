package com.jfk.mapper;

import com.jfk.base.mapper.MyMapper;
import com.jfk.domain.Score;

import org.apache.ibatis.annotations.Select;

/**
 * Created by wcg on 2016/12/14.
 */
public interface ScoreMapper extends MyMapper<Score> {

  @Select(value = "select * from score where user_id = #{userId}")
  Score selectByUserId(Long userId);
}
