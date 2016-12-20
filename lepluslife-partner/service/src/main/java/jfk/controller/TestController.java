package jfk.controller;

import com.jfk.dto.PartnerConsistent;
import com.jfk.dto.PartnerDto;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import jfk.domain.Partner;
import jfk.service.PartnerService;

/**
 * Created by wcg on 2016/11/13.
 */
@RestController
public class TestController {

  @Inject
  private ModelMapper modelMapper;

  @Inject
  private PartnerService partnerService;

  @RequestMapping("/partner/id")
  public PartnerDto findPartnerById(@PathVariable Long id) {
    return convertToDto(partnerService.findPartnerById(id));
  }

  @RequestMapping(value = "/partner/distribute", method = RequestMethod.POST)
  public void testDistribute(@RequestBody PartnerConsistent partnerConsistent) {
    partnerService.testDistribute(convertToEntity(partnerConsistent.getPartnerDto()),
                                  partnerConsistent.getTransactionLock());
  }

  private PartnerDto convertToDto(Partner partner) {
    return modelMapper.map(partner, PartnerDto.class);
  }

  private Partner convertToEntity(PartnerDto partnerDto) {
    return modelMapper.map(partnerDto, Partner.class);
  }
}
