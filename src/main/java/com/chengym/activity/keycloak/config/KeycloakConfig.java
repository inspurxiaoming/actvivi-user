package com.chengym.activity.keycloak.config;

import com.chengym.active.keycloak.Keycloak;
import com.chengym.active.keycloak.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;
import static org.keycloak.OAuth2Constants.PASSWORD;

@Configuration
public class KeycloakConfig {

	@Autowired
	private ConfigProperties configProperties;

	public Keycloak getKeyCloak() {
		Keycloak kc = KeycloakBuilder.builder().serverUrl(configProperties.getServerUrl())
				.realm(configProperties.getRealm()).username(configProperties.getUsername())
				.password(configProperties.getPassword()).clientId(configProperties.getClientId())
				.clientSecret(configProperties.getClientSecret()).grantType(PASSWORD).build();
		return kc;
	}
	@Bean
	public Keycloak getCKeyCloak() {
		Keycloak kc = KeycloakBuilder.builder().serverUrl(configProperties.getServerUrl())
				.realm(configProperties.getRealm()).clientId(configProperties.getClientId())
				.clientSecret(configProperties.getClientSecret()).grantType(CLIENT_CREDENTIALS).build();
		return kc;
	}
}
