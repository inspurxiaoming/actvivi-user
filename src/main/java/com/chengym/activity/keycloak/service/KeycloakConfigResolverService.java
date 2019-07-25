package com.chengym.activity.keycloak.service;

import com.chengym.active.keycloak.Keycloak;
import com.chengym.active.keycloak.config.ConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;
import static org.keycloak.OAuth2Constants.PASSWORD;

@Service
public class KeycloakConfigResolverService {
	@Autowired
	ConfigProperties configProperties;

	public static final Logger LOGGER = LoggerFactory.getLogger(KeycloakConfigResolverService.class);

	public Object getClientConfigJSON(String path) throws IOException {
		if (isRequestFromLocal(path)) {
			File file = getConfigFile("dev");
			LOGGER.info("getClientConfigJSON : requested from local machine and origin is : {} and returning file path is {}", path,
					file.toPath());
			String content = getAsString(file);
			return content;
		}
		String realm = getRealmName(path);
		File file = getConfigFile(realm);
		String content = getAsString(file);
		return content;
	}

	public InputStream getClientConfigIS(String path) throws FileNotFoundException {
		if (isRequestFromLocal(path)) {
			File file = getConfigFile("dev");
			LOGGER.info("getClientConfigIS : requested from local machine and origin is : {} and returning file path is {}", path,
					file.toPath());
			InputStream content = getAsStream(file);
			return content;
		}
		String realm = getRealmName(path);
		File file = getConfigFile(realm);
		InputStream content = getAsStream(file);
		return content;
	}

	private String getRealmName(String path) {
		String[] domains = path.split("\\.");
		LOGGER.info("request origin is : {} and split by . is : {}", path, domains);
		if (domains.length < 3) {
			throw new IllegalStateException("Not able to resolve realm from the request path!");
		}
		String realm = domains[0];
		return realm;
	}

	private boolean isRequestFromLocal(String path) {
		return path.indexOf("localhost") != -1;
	}

	private InputStream getAsStream(File file) throws FileNotFoundException {
		InputStream inputStream = new FileInputStream(file);
		return inputStream;
	}

	private String getAsString(File file) throws IOException {
		String content = new String(Files.readAllBytes(file.toPath()));
		return content;
	}

	private File getConfigFile(String realm) throws FileNotFoundException {
		File file = ResourceUtils.getFile("classpath:" + realm + "keycloak.json");
		return file;
	}
	public String getClientToken(){
		Keycloak keycloak = new Keycloak( configProperties.getOperationUrl(),  configProperties.getOperationRealm(),  configProperties.getOperationId(),  configProperties.getOperationSecret(),
				CLIENT_CREDENTIALS);
		return keycloak.tokenManager().getAccessTokenString();
	}
	public String getClientToken(String operationUrl,String operationRealm,String operationId,String operationSecret){
		Keycloak keycloak = new Keycloak( operationUrl,  operationRealm,  operationId,  operationSecret,CLIENT_CREDENTIALS);
		return keycloak.tokenManager().getAccessTokenString();
	}
	public Keycloak getKeycloak(){
		return new Keycloak( configProperties.getOperationUrl(),  configProperties.getOperationRealm(),  configProperties.getOperationId(),  configProperties.getOperationSecret(),
				CLIENT_CREDENTIALS);
	}
	public Keycloak getKeycloak(String username, String password){
		return new Keycloak( configProperties.getOperationUrl(),  configProperties.getOperationRealm(),username,password, configProperties.getOperationId(),  configProperties.getOperationSecret(),
				PASSWORD);
	}
}
