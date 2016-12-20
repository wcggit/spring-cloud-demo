package com.jfk.service;

import com.jfk.base.api.BooleanWrapper;
import com.jfk.domain.Score;
import com.jfk.mapper.ScoreMapper;
import com.jfk.mapper.UserMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by wcg on 2016/12/14.
 */
@Service
@Transactional
public class ScoreService {

  @Inject
  private ScoreMapper scoreMapper;


  public void revokeBalance(Long userId, Long scoreA) {
    Score score = scoreMapper.selectByUserId(userId);
    score.setScore(score.getScore() + scoreA);
    scoreMapper.updateByPrimaryKey(score);
  }

  public BooleanWrapper processScoreBalance(Long userId, Long scoreA) {
    Score score = scoreMapper.selectByUserId(userId);
    if (score.getScore() >= scoreA) {
      score.setScore(score.getScore() - scoreA);
      if (scoreMapper.updateByPrimaryKey(score) == 1) {
        return new BooleanWrapper(true, "OK");
      }
    }
    return new BooleanWrapper(false, "余额不足");
  }
}
