package com.chengym.activity.common.exception.handler;

import com.chengym.active.common.bean.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author sunguangtao 2018-10-13
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler extends BaseExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseBean handleUnKnownException(Exception ex) {
        return wrapper(ex);
    }
}
