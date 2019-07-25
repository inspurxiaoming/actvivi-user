package com.chengym.activity.common.bean;


import com.chengym.active.common.exception.model.ErrorCodeEntity;

/**
 * Desc:   通用返回值
 */
public class ResponseBean<T> {
	public static final String CODE_SUCCESS = "0";
	public static final String MESSAGE_SUCCESS = "请求成功";

	/**
     * 返回码 0成功，其他失败
     */
    private String code;
    /**
     * 描述信息
     */
    private String message;
    /**
     * 返回结果
     */
    private T result;
    
    public ResponseBean() {}
    
    /**
     * 通过结果对象直接获得正确的返回结果
     * @author daiyan
     * @param result 返回数据
     */
    public ResponseBean(T result) {
    	this.code = CODE_SUCCESS;
    	this.message = MESSAGE_SUCCESS;
    	this.result = result;
    }
    
    /**
     * 通过错误码对象直接获得正确的返回结果
     * @author daiyan
     * @param errorCode ErrorCodeEntity
     */
    public ResponseBean(ErrorCodeEntity errorCode) {
    	this.code = errorCode.getProjectCode() + "." + errorCode.getModuleCode() + errorCode.getErrorCode();
    	this.message = errorCode.getErrorMessage();
    	this.result = null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
