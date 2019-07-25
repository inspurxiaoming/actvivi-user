package com.chengym.activity.keycloak.token;

import org.keycloak.representations.AccessTokenResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public interface TokenService {

    @POST
    @Path("/realms/{realm}/protocol/openid-connect/token")
    AccessTokenResponse grantToken(@PathParam("realm") String realm, MultivaluedMap<String, String> map);

    @POST
    @Path("/realms/{realm}/protocol/openid-connect/token")
    AccessTokenResponse refreshToken(@PathParam("realm") String realm, MultivaluedMap<String, String> map);

}