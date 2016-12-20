package com.jfk.client;

import com.jfk.dto.OffLineOrderDto;
import com.jfk.dto.OfflineOrderConsistent;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Created by wcg on 2016/12/2.
 */
@FeignClient("ORDER-SERVICE")
public interface OffLineOrderClient {

  @RequestMapping("/order/{id}")
  OffLineOrderDto findOrderById(@PathVariable("id") Long id);

  @RequestMapping("/order")
  OffLineOrderDto testEventDistributeTransaction();

  @RequestMapping(value = "/order/distribute",method = RequestMethod.POST,consumes = "application/json")
  OffLineOrderDto test2pcDistributeTransaction(@RequestBody OfflineOrderConsistent offlineOrderConsistent);


}
