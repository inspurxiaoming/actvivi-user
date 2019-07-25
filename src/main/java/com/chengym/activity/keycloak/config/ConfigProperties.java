package com.chengym.activity.keycloak.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.validation.constraints.NotBlank;

@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "keycloak")
public class ConfigProperties {

	@NotBlank
	@Value("${keycloak.auth-server-url}")
	private String serverUrl;

	@NotBlank
	@Value("jindengke")
	private String username;

	@NotBlank
	@Value("123456a?")
	private String password;

	@Value("${keycloak.resource}")
	private String clientId = "bss-server";

	@Value("${keycloak.credentials.secret}")
	private String clientSecret = "060ae88a-bb7c-4fec-abdd-3c482cc184ac";

	@Value("${keycloak.realm}")
	private String realm = "picp";
	private int connectionPoolSize = 10;

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public int getConnectionPoolSize() {
		return connectionPoolSize;
	}

	public void setConnectionPoolSize(int connectionPoolSize) {
		this.connectionPoolSize = connectionPoolSize;
	}

	@Override
	public String toString() {
		return "ConfigProperties [serverUrl=" + serverUrl + ", username=" + username + ", password=" + password
				+ ", clientId=" + clientId + ", clientSecret=" + clientSecret + ", realm=" + realm
				+ ", connectionPoolSize=" + connectionPoolSize + "]";
	}
	@Value("${keycloak.auth-server-url}")
	private String operationUrl;
	@Value("${keycloak.realm}")
	private String operationRealm;
	@Value("${keycloak.credentials.secret}")
	private String operationSecret;
	@Value("${keycloak.resource}")
	private String operationId;

	public String getOperationUrl() {
		return operationUrl;
	}

	public void setOperationUrl(String operationUrl) {
		this.operationUrl = operationUrl;
	}

	public String getOperationRealm() {
		return operationRealm;
	}

	public void setOperationRealm(String operationRealm) {
		this.operationRealm = operationRealm;
	}

	public String getOperationSecret() {
		return operationSecret;
	}

	public void setOperationSecret(String operationSecret) {
		this.operationSecret = operationSecret;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

}