package jfk.service;

import com.jfk.base.api.BooleanWrapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import jfk.domain.PartnerScore;
import jfk.mapper.PartnerScoreMapper;

/**
 * Created by wcg on 2016/12/14.
 */
@Service
@Transactional
public class PartnerScoreService {

  @Inject
  private PartnerScoreMapper scoreMapper;


  public void revokeBalance(Long partnerId, Long scoreA) {
    PartnerScore score = scoreMapper.selectByPartnerId(partnerId);
    score.setScore(score.getScore() + scoreA);
    scoreMapper.updateByPrimaryKey(score);
  }

  public BooleanWrapper processScoreBalance(Long partnerId, Long scoreA) {
    PartnerScore score = scoreMapper.selectByPartnerId(partnerId);
    if (score.getScore() >= scoreA) {
      score.setScore(score.getScore() - scoreA);
      if (scoreMapper.updateByPrimaryKey(score) == 1) {
        return new BooleanWrapper(true, "OK");
      }
    }
    return new BooleanWrapper(false, "余额不足");
  }
}
