package com.chengym.activity.common.exception;

import com.chengym.active.common.bean.ResponseBean;
import lombok.Data;

/**
 * All self-defined extended exception must inherit BaseException class.
 *
 * @author sunguangtao 2018-10-13
 */
@Data
public class BaseException extends RuntimeException {
    protected String code;
    protected String message;

    public BaseException() {
    }

    public BaseException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseBean generateResponseBean() {
        ResponseBean responseBean = new ResponseBean();
        //TODO message get from db
        responseBean.setMessage(this.message);
        responseBean.setCode(this.code);
        return responseBean;
    }
}
