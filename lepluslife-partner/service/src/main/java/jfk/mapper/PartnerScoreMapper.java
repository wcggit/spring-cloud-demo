package jfk.mapper;

import com.jfk.base.mapper.MyMapper;

import org.apache.ibatis.annotations.Select;

import jfk.domain.PartnerScore;

/**
 * Created by wcg on 2016/12/15.
 */
public interface PartnerScoreMapper extends MyMapper<PartnerScore> {

  @Select(value = "select * from partner_score where partner_id = #{partnerId}")
  PartnerScore selectByPartnerId(Long partnerId);
}
