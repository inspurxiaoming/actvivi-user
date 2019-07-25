package com.chengym.activity.common.exception.handler;

import com.chengym.active.common.bean.ResponseBean;
import com.chengym.active.common.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * @author sunguangtao 2018-10-13
 */
public class BaseExceptionHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    protected ResponseBean exceptionEntity = new ResponseBean();

    protected ResponseBean wrapper(BaseException ex) {
        exceptionEntity.setCode(ex.getCode());
        exceptionEntity.setMessage(ex.getMessage());
        return exceptionEntity;
    }

    protected ResponseBean wrapper(HttpStatus httpStatus) {
        exceptionEntity.setMessage(httpStatus.name());
        exceptionEntity.setCode(httpStatus.toString());
        return exceptionEntity;
    }

    protected ResponseBean wrapper(Exception ex) {
        //SecurityContextUtil getLoginUser 抛出异常
        if (ex instanceof BaseException) {
            return ((BaseException) ex).generateResponseBean();
        } else {
            exceptionEntity.setMessage("internal error!");
            exceptionEntity.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            logger.error("internall error", ex);
        }
        return exceptionEntity;
    }
}
