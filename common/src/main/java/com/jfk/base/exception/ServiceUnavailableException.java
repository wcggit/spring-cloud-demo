package com.jfk.base.exception;


import com.jfk.base.api.ErrorCode;
import com.jfk.base.api.CommonErrorCode;

/**
 * Created by liubin on 2016/5/3.
 */
public class ServiceUnavailableException extends AppBusinessException {

    private static final ErrorCode ERROR_CODE = CommonErrorCode.SERVICE_UNAVAILABLE;

    public ServiceUnavailableException(String message) {
        super(ERROR_CODE.getCode(), ERROR_CODE.getStatus(), " 远程服务不可用: " + message);
    }

}
