package com.chengym.activity.common;

import com.chengym.active.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.UUID;


@Component
@Slf4j
public class OwnRestTemplate<T> extends RestTemplate {
    @Autowired
    private HttpServletRequest request;

    public OwnRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5 * 60 * 1000);// 设置超时
        requestFactory.setReadTimeout(5 * 60 * 1000);
        super.setRequestFactory(requestFactory);
    }
    public OwnRestTemplate(HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory){
        super(httpComponentsClientHttpRequestFactory);
    }

    public OwnRestTemplate(HttpsRequestFactory httpsRequestFactory){
        super.setRequestFactory(httpsRequestFactory);
    }

    public ResponseEntity<T> exchange(String tokenString, String url, HttpMethod method,
                                      Object params, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = generateHeaders(tokenString);
        HttpEntity entity = new HttpEntity(params, httpHeaders);
        try {
            ResponseEntity<T> result = super.exchange(url, method, entity, responseType, uriVariables);
            return result;
        } catch (Exception e) {
            log.error("ERROR", "access api failure", e);
            return null;
        }
    }


    public HttpHeaders generateHeaders(String tokenString) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + tokenString);
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        String requestId = request.getHeader(RequestIdUtil.REQUEST_ID_KEY);
        if (StringUtils.isEmpty(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        httpHeaders.add(RequestIdUtil.REQUEST_ID_KEY, requestId);
        return httpHeaders;
    }

    public ResponseEntity<T> exchange(String url, HttpMethod method,
                                      Object params, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = SecurityContextUtil.getHeaders();
        HttpEntity entity = new HttpEntity(params, httpHeaders);
        try {
            ResponseEntity<T> result = super.exchange(url, method, entity, responseType, uriVariables);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<T> exchangeWIthOutAuth(String url, HttpMethod method,
                                                 Object params, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = SecurityContextUtil.getHeadersWithoutAuth();
        HttpEntity entity = new HttpEntity(params, httpHeaders);
        try {
            ResponseEntity<T> result = super.exchange(url, method, entity, responseType, uriVariables);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<T> exchanges(String url, HttpMethod method,
                                       Object params, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = SecurityContextUtil.getHeader();
        HttpEntity entity = new HttpEntity(params, httpHeaders);
        try {
            ResponseEntity<T> result = super.exchange(url, method, entity, responseType, uriVariables);
            return result;
        } catch (Exception e) {
            log.error("ERROR", "access api failure", e);
            return null;
        }
    }
}
