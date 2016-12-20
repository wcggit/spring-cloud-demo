package jfk.service;

import com.jfk.distribute.annotation.DistributeTransaction;
import com.jfk.distribute.entities.TransactionLock;
import com.jfk.dto.PartnerDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import jfk.domain.Partner;
import jfk.mapper.PartnerMapper;

/**
 * Created by wcg on 2016/12/20.
 */
@Service
@Transactional
public class PartnerService {

  @Inject
  private PartnerMapper partnerMapper;

  public Partner findPartnerById(Long id) {
    return partnerMapper.selectByPrimaryKey(id);
  }

  @DistributeTransaction
  public void testDistribute(Partner partner, TransactionLock transactionLock) {
    partnerMapper.insert(partner);
  }
}
