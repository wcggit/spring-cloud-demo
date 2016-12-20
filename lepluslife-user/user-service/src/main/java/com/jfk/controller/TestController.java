package com.jfk.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jfk.common.distribute.event.domain.UserCreatedEvent;
import com.jfk.common.event.service.EventBus;
import com.jfk.distribute.entities.TransactionLock;
import com.jfk.domain.User;
import com.jfk.dto.UserConsistent;
import com.jfk.dto.UserDto;
import com.jfk.mapper.UserMapper;
import com.jfk.service.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wcg on 2016/11/13.
 */
@RestController
public class TestController {

  @Autowired
  private UserMapper userMapper;

  @Inject
  private UserService userService;

  @Inject
  private ModelMapper modelMapper;

  @Inject
  private EventBus eventBus;


  @RequestMapping("/")
  public UserDto getUser() {
    System.out.println("1");
    eventBus.publish(new UserCreatedEvent(1L, "12"));
    return modelMapper.map(userMapper.getUserById(2L), UserDto.class);
  }

  @RequestMapping(value = "/user/{page}/{size}", method = RequestMethod.GET)
  public List<UserDto> getUsersByPage(@PathVariable int page, @PathVariable int size) {
    PageHelper.startPage(page, size);
    List<User> users = userMapper.findAll();
    Long count = ((Page) users).getTotal();
    return users.stream().map(user -> {
      return modelMapper.map(user, UserDto.class);
    }).collect(Collectors.toList());
  }

  @RequestMapping("/user/update")
  public String updateShow() {
    userService.userManage();
    return "ok";
  }


  @RequestMapping("/user/distribute")
  public void updateShow(@RequestBody UserConsistent userConsistent) {
    userService.distributeManage(modelMapper.map(userConsistent.getUserDto(), User.class),
                                 userConsistent.getTransactionLock());
  }
}
