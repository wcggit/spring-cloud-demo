package com.jfk.mapper;

import com.jfk.base.mapper.MyMapper;
import com.jfk.domain.User;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by wcg on 2016/11/13.
 */
public interface UserMapper extends MyMapper<User> {

  @Select(value = "select * from user where id = #{id}")
  User getUserById(Long id);

  @Select("select * from user")
  List<User> findAll();

  @Select("select * from user where id = 9 for update")
  User exclusiveLock();
}
