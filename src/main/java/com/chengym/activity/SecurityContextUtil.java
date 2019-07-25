package com.chengym.activity;

import com.chengym.active.common.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityContextUtil {

	private static User defaultUser = null;
	static{
	    if(SecurityContextUtil.getLoginUser()!=null){
				defaultUser = SecurityContextUtil.getLoginUser();
        }else{
        }
	}
	
	public static User getLoginUser(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(null != authentication){
			log.info("Class SecurityContextUtil get Authentication:{}",authentication.getPrincipal());
		}
		if(authentication instanceof KeycloakAuthenticationToken){
			KeycloakAuthenticationToken tokenObj = (KeycloakAuthenticationToken) authentication;
			if(!tokenObj.isAuthenticated()){
				throw new RuntimeException("Authenticated:false");
			}
			AccessToken token = tokenObj.getAccount().getKeycloakSecurityContext().getToken();
			log.info("token is :{}",token.getOtherClaims());
			//用户内码
			String sub = token.getSubject();
			String loginName = token.getPreferredUsername();
			String email = token.getEmail();
			User loginUser = new User();
			loginUser.setName(loginName);
			loginUser.setId(StringUtils.isEmpty(sub)?"":sub.trim());
			loginUser.setEmail(email);
			loginUser.setRoles(token.getRealmAccess().getRoles());
			loginUser.setDisplayName(token.getGivenName());
			loginUser.setGivenName(token.getGivenName());
			loginUser.setFamilyName(token.getFamilyName());
			return loginUser;
		}
		return null;
	}
	public static HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof KeycloakAuthenticationToken){
            KeycloakAuthenticationToken tokenObj = (KeycloakAuthenticationToken) authentication;
            if(!tokenObj.isAuthenticated()){
                throw new RuntimeException("Authenticated:false");
            }
            String tokenString = tokenObj.getAccount().getKeycloakSecurityContext().getTokenString();
            httpHeaders.add("Authorization", "Bearer " + tokenString);
			httpHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
			httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        }
         return httpHeaders;
    }

	public static HttpHeaders getHeadersWithoutAuth() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		return httpHeaders;
	}

	public static HttpHeaders getHeader(){
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		return httpHeaders;
	}
	public static void setLoginUser(User user){
		defaultUser = user;
	}

}
