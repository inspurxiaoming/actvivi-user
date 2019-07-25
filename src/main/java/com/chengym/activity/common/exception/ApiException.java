package com.chengym.activity.common.exception;

/**
 * Exception thrown when invoke api of other module
 *
 * @author sunguangtao 2018-10-13
 */
public class ApiException extends BaseException {
    public ApiException(String code, String message) {
        super(code, message);
    }
}
