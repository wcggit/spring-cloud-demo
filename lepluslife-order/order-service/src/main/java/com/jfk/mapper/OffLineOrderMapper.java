package com.jfk.mapper;

import com.jfk.base.mapper.MyMapper;
import com.jfk.domain.OffLineOrder;

import org.apache.ibatis.annotations.Select;

/**
 * Created by wcg on 2016/12/2.
 */
public interface OffLineOrderMapper extends MyMapper<OffLineOrder> {

  @Select(value = "select * from off_line_order where order_sid = #{orderSid}")
  OffLineOrder selectByOrderSid(String orderSid);
}
