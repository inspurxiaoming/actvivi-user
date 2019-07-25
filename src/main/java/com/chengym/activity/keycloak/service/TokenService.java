package com.chengym.activity.keycloak.service;

import com.alibaba.fastjson.JSONObject;
import com.chengym.active.common.HttpsRequestFactory;
import com.chengym.active.common.OwnRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;

@Service
@Slf4j
public class TokenService {
    public String getClientToken(String userServiceAuthUrl,String realm,String clientId,String clientSecret){
        OwnRestTemplate ownRestTemplate = new OwnRestTemplate(new HttpsRequestFactory());
        String url = userServiceAuthUrl + "/realms/"+realm+"/protocol/openid-connect/token";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        MultiValueMap<String, Object> parameterMap = new LinkedMultiValueMap<>();
        parameterMap.add("grant_type", CLIENT_CREDENTIALS);
        parameterMap.add("client_id", clientId);
        parameterMap.add("client_secret", clientSecret);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parameterMap,httpHeaders);
        try{
            ResponseEntity<JSONObject> resEntity = ownRestTemplate.exchangeWIthOutAuth(url, HttpMethod.POST, httpEntity, JSONObject.class);
            return resEntity.getBody().getString("access_token");
        }catch (Exception e){
            ResponseEntity<JSONObject> resEntity = ownRestTemplate.postForEntity(url, httpEntity, JSONObject.class);
            return resEntity.getBody().getString("access_token");
        }
    }
}