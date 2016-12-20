package com.jfk.client;

import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Created by wcg on 2016/12/20.
 */
@FeignClient("PARTNER-SERVICE")
public interface PartnerClient  {

}
