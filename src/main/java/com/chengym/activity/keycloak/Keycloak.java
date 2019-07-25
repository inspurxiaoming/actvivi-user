package com.chengym.activity.keycloak;

import com.chengym.active.keycloak.resource.RealmResource;
import com.chengym.active.keycloak.resource.RealmsResource;
import com.chengym.active.keycloak.resource.ServerInfoResource;
import com.chengym.active.keycloak.token.TokenManager;
import com.chengym.active.keycloak.util.BearerAuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;


@Slf4j
public class Keycloak {

    private final Config config;

    private final Client client;

    private final WebTarget target;

    public Keycloak(String serverUrl, String realm, String username, String password, String clientId, String clientSecret,
             String grantType) {
        config = new Config(serverUrl, realm, username, password, clientId, clientSecret, grantType);
        client = ClientBuilder.newClient().register(JacksonFeature.class);
        target = client.target(config.getServerUrl());
        target.register(newAuthFilter());
    }
    public Keycloak(String serverUrl, String realm, String clientId, String clientSecret,
             String grantType) {
        config = new Config(serverUrl, realm, clientId, clientSecret, grantType);
        client = ClientBuilder.newClient().register(JacksonFeature.class);
        target = client.target(config.getServerUrl());
        target.register(newAuthFilter());
    }

    private TokenManager getTokenManager() {
        return new TokenManager(config, client);
    }

    private BearerAuthFilter newAuthFilter() {
        return new BearerAuthFilter(getTokenManager());
    }

    public RealmsResource realms() {
        return WebResourceFactory.newResource(RealmsResource.class, target);
    }

    public RealmResource realm(String realmName) {
        return realms().realm(realmName);
    }

    public ServerInfoResource serverInfo() {
        log.info("serverInfo");
        return WebResourceFactory.newResource(ServerInfoResource.class, target);
    }

    public TokenManager tokenManager() {
        log.info("tokenManager");
        return getTokenManager();
    }

    public void close() {
        client.close();
    }

}