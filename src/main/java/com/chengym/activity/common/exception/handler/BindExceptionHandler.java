package com.chengym.activity.common.exception.handler;

import com.chengym.active.common.bean.ResponseBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * handler for exception thrown by restful controller when binding request parameter
 *
 * @author sunguangtao 2018-10-12
 */
@Slf4j
@RestControllerAdvice
public class BindExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBean<List<ParameterValidationMessage>> handleBindException(MethodArgumentNotValidException ex) {
        ResponseBean<List<ParameterValidationMessage>> errorResult = new ResponseBean<>();
        errorResult.setCode(HttpStatus.BAD_REQUEST.toString());
        errorResult.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());

        List<ParameterValidationMessage> validationMessages = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationMessages.add(new ParameterValidationMessage(fieldError));
        }
        errorResult.setResult(validationMessages);
        return errorResult;
    }
}

@Data
class ParameterValidationMessage {
    private String description;
    private String parameter;
    private Object value;
    private String code;

    ParameterValidationMessage(FieldError fieldError) {
        this.description = fieldError.getDefaultMessage();
        this.parameter = fieldError.getField();
        this.code = fieldError.getCode();
        this.value = fieldError.getRejectedValue();
    }
}
