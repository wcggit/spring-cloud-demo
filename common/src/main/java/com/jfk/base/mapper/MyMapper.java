package com.jfk.base.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by wcg on 2016/11/14.
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
