package com.jfk.controller;

import com.jfk.distribute.entities.TransactionLock;
import com.jfk.domain.OffLineOrder;
import com.jfk.dto.OffLineOrderDto;
import com.jfk.dto.OfflineOrderConsistent;
import com.jfk.service.OrderService;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by wcg on 2016/11/13.
 */
@RestController
public class OffLineOrderController {


  @Inject
  private OrderService orderService;

  @Inject
  private ModelMapper modelMapper;

  @RequestMapping("/order/{id}")
  public OffLineOrderDto getOrderById(@PathVariable Long id) {
    return convertToDto(orderService.findOrderById(id));
  }

  @RequestMapping("/order")
  public void testDistributeTransaction() {
    orderService.payOffLineOrder();
  }

  @RequestMapping("/order/share")
  public void testDoubleDistributeTransaction() {
    orderService.payOffLineOrderShareToPartner();
  }

  @RequestMapping(value = "/order/distribute", method = RequestMethod.POST)
  public void test2PcDistributeTransaction(
      @RequestBody OfflineOrderConsistent offlineOrderConsistent) {
    orderService.test2PcDistribute(convertToEntity(offlineOrderConsistent.getOffLineOrderDto()),
                                   offlineOrderConsistent.getTransactionLock());
  }


  private OffLineOrderDto convertToDto(OffLineOrder offLineOrder) {
    return modelMapper.map(offLineOrder, OffLineOrderDto.class);
  }

  private OffLineOrder convertToEntity(OffLineOrderDto offLineOrderDto) {
    return modelMapper.map(offLineOrderDto, OffLineOrder.class);
  }

}
