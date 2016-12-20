package com.jfk.client;

import com.jfk.dto.UserConsistent;
import com.jfk.dto.UserDto;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

import feign.QueryMap;

/**
 * Created by wcg on 2016/11/29.
 */
@FeignClient("USER-SERVICE")
public interface UserClient {

  @RequestMapping(value = "/user/{page}/{size}", method = RequestMethod.GET)
  List<UserDto> getUsersByPage(@PathVariable("page") int page, @PathVariable("size") int size);

  @RequestMapping(value = "/", method = RequestMethod.GET)
  UserDto getUser();

  @RequestMapping(value = "/user/distribute", method = RequestMethod.POST,consumes = "application/json")
  UserDto transaction(@RequestBody UserConsistent userConsistent);
}
