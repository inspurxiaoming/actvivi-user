package com.chengym.activity.keycloak.service;

import com.chengym.active.common.Constants.MessageConstants;
import com.chengym.active.common.OwnRestTemplate;
import com.chengym.active.common.exception.ApiException;
import com.chengym.active.keycloak.Keycloak;
import com.chengym.active.keycloak.config.ConfigProperties;
import com.chengym.active.keycloak.exception.KeyCloakServiceException;
import com.chengym.active.keycloak.model.KeyCloakRole;
import com.chengym.active.keycloak.model.KeycloakUser;
import com.chengym.active.keycloak.resource.UserResource;
import com.chengym.active.keycloak.token.TokenManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class KeycloakUserService {

    public static final Logger LOGGER = LoggerFactory.getLogger(KeycloakUserService.class);

    @Autowired
    private Keycloak keycloak;
    @Autowired
    OwnRestTemplate ownRestTemplate;
    @Autowired
    ConfigProperties configProperties;
    @Autowired
    KeycloakConfigResolverService keycloakConfigResolverService;
    @Autowired
    TokenService tokenService;

    @Value("${keycloak.realm}")
    private String realm = "picp";

    @Value("${keycloak.auth-server-url}")
    private String userServiceAuthUrl;
    @Value("${keycloak.realm}")
    private String userServiceAuthRealm;
    @Value("${keycloak.credentials.secret}")
    private String userServiceClientSecret;
    @Value("${keycloak.resource}")
    private String userServiceClientId;


    public String create(KeycloakUser keycloakUser) {
        UserRepresentation user = getUserRepresentation(keycloakUser);
        Response result = keycloakConfigResolverService.getKeycloak().realm(realm).users().create(user);
        if (result.getStatus() != HttpStatus.CREATED.value()) {
            LOGGER.info("Unable to create user. Keycloak responded with status {}", result.getStatus());
            if (result.getStatus() == HttpStatus.CONFLICT.value()) {
                throw new ApiException(String.valueOf(result.getStatus()), "该用户已经注册，不能重复注册");
            } else {
                throw new KeyCloakServiceException(
                        "Unable to create user. Keycloak responded with status " + result.getStatus());
            }
        }
        String userId = getCreatedUserId(result);
        keycloakUser.setKeyCloakUserId(userId);
        //TODO update user to set roles
//		updateRoles(keycloakUser, realm);
        return userId;
    }

    public Response updatePassword(KeycloakUser keycloakUser) {
        UserResource userResource = getUserResourceByUserId(realm, keycloakUser.getKeyCloakUserId());
        CredentialRepresentation credential = getCredentials(keycloakUser);
        return userResource.resetPassword(credential);
    }

    /**
     * Call keycloak to change user information
     *
     * @param keycloakUser
     * @return
     */
    public boolean update(KeycloakUser keycloakUser) {
        realm = "picp";
        log.info("call keycloak to change user information,keycloakUser info:{}", keycloakUser.toString());
        String token = keycloakConfigResolverService.getClientToken();
        log.info("Client Token:{}", token);
        String url = configProperties.getOperationUrl() + "/admin/realms/" + configProperties.getOperationRealm() + "/users/" + keycloakUser.getKeyCloakUserId();
        log.info("OperationUrl:{}", url);
        UserResource userResource = getUserResourceByUserId(realm, keycloakUser.getKeyCloakUserId());
        log.info("call getUserResourceByUserId(), result userResource info:{}", userResource.toString());
        UserRepresentation representation = userResource.toRepresentation();
        log.info("get representation by toRepresentation()");
        representation.setEnabled(keycloakUser.isEnabled());
        checkDiffBetweenUserMessage(keycloakUser, representation);
        log.info("set representation enable and checkDiffBetweenUserMessage(),call keycloak to change user information");
        ResponseEntity<Response> result = ownRestTemplate.exchange(token, url, HttpMethod.PUT, representation, Response.class, representation);
        log.info("the result of call keycloak to change user information , result:{}", result == null ? "" : result.toString());
        return result != null && HttpStatus.NO_CONTENT.value() == result.getStatusCodeValue();
    }
    /**
     * Call keycloak to change user information
     *
     * @param keycloakUser
     * @return
     */
    public boolean updateUserInfo(KeycloakUser keycloakUser,String realm) {
        log.info("call keycloak to change user information,keycloakUser info:{}", keycloakUser.toString());
        String token = keycloakConfigResolverService.getClientToken();
        log.info("Client Token:{}", token);
        String url = configProperties.getOperationUrl() + "/admin/realms/" + configProperties.getOperationRealm() + "/users/" + keycloakUser.getKeyCloakUserId();
        log.info("OperationUrl:{}", url);
        UserResource userResource = getUserResourceByUserId(realm, keycloakUser.getKeyCloakUserId());
        log.info("call getUserResourceByUserId(), result userResource info:{}", userResource.toString());
        UserRepresentation representation = userResource.toRepresentation();
        log.info("get representation by toRepresentation()");
        representation.setEnabled(keycloakUser.isEnabled());
        checkDiffBetweenUserMessage(keycloakUser, representation);
        log.info("set representation enable and checkDiffBetweenUserMessage(),call keycloak to change user information");
        ResponseEntity<Response> result = ownRestTemplate.exchange(token, url, HttpMethod.PUT, representation, Response.class, representation);
        log.info("the result of call keycloak to change user information , result:{}", result == null ? "" : result.toString());
        return result != null && HttpStatus.NO_CONTENT.value() == result.getStatusCodeValue();
    }
    /**
     * Check the difference between user information
     * *********Attributes can change mobile only
     *
     * @param keycloakUser
     * @param representation
     */
    private void checkDiffBetweenUserMessage(KeycloakUser keycloakUser, UserRepresentation representation) {
        if (!StringUtils.isEmpty(keycloakUser.getEmail())) {
            if (!keycloakUser.getEmail().equals(representation.getEmail())) {
                representation.setEmail(keycloakUser.getEmail());
            }
        }
        if (!StringUtils.isEmpty(keycloakUser.getUserName())) {
            if (!keycloakUser.getUserName().equals(representation.getUsername())) {
                representation.setUsername(keycloakUser.getUserName());
            }
        }
        if (null != keycloakUser.getAttributes()) {
            Map<String, List<String>> map = representation.getAttributes();
            if(null == map){
                map = new HashMap<String, List<String>>();
            }
            map.put(MessageConstants.PHONE, keycloakUser.getAttributes().get(MessageConstants.PHONE));
            representation.setAttributes(map);
        }
    }

    public KeycloakUser getUser(String realm, String userId) {
        UserResource userResource = getUserResourceByUserId(realm, userId);
        KeycloakUser keycloakUser = getKeyCloakUser(userResource, realm);
        return keycloakUser;
    }

    public List<KeycloakUser> listUsers(String realm) {
        List<UserRepresentation> users = keycloak.realm(realm).users().search("", 0, 100);
        List<KeycloakUser> keycloakUsers = new ArrayList<>();
        users.stream().forEach(user -> {
            UserResource userResource = getUserResourceByUserId(realm, user.getId());
            keycloakUsers.add(getKeyCloakUser(userResource, realm));
        });
        return keycloakUsers;
    }

    public List<KeyCloakRole> getUnassignedRolesByUser(String realm, String userId) {
        UserResource userResource = getUserResourceByUserId(realm, userId);
        List<RoleRepresentation> list = userResource.roles().realmLevel().listAvailable();
        List<KeyCloakRole> roles = convert(list);
        return roles;
    }

    public KeyCloakRole getRole(String realm, String roleId) {
        RoleRepresentation representation = keycloak.realm(realm).rolesById().getRole(roleId);
        KeyCloakRole cloakRole = convert(representation);
        return cloakRole;
    }

    public List<KeyCloakRole> listRoles(String realm) {
        List<RoleRepresentation> list = keycloakConfigResolverService.getKeycloak().realm(realm).roles().list();
        List<KeyCloakRole> roles = convert(list);
        return roles;
    }

    private List<KeyCloakRole> convert(List<RoleRepresentation> list) {
        List<KeyCloakRole> roles = new ArrayList<>();
        list.stream().forEach(r -> {
            roles.add(convert(r));
        });
        return roles;
    }

    private KeyCloakRole convert(RoleRepresentation role) {
        KeyCloakRole cloakRole = new KeyCloakRole();
        cloakRole.setId(role.getId());
        cloakRole.setName(role.getName());
        return cloakRole;
    }

    private List<RoleRepresentation> getUnassignedRolesByUserId(String realm, String userId) {
        UserResource userResource = getUserResourceByUserId(realm, userId);
        List<RoleRepresentation> list = userResource.roles().realmLevel().listAvailable();
        return list;
    }

    private void updateRoles(KeycloakUser keycloakUser, String realm) {
        List<RoleRepresentation> rolesToAssign = getRolesToAssign(keycloakUser, realm);
        List<RoleRepresentation> rolesToRemove = getRolesToRemove(keycloakUser, realm);
        UserResource userResource = getUserResourceByUserId(realm, keycloakUser.getKeyCloakUserId());
        userResource.roles().realmLevel().add(rolesToAssign);
        userResource.roles().realmLevel().remove(rolesToRemove);
    }

    private List<RoleRepresentation> getRolesToAssign(KeycloakUser keycloakUser, String realm) {
        List<RoleRepresentation> unassignedRoles = getUnassignedRolesByUserId(realm, keycloakUser.getKeyCloakUserId());
        List<String> roleIds = keycloakUser.getRoleIds();
        List<RoleRepresentation> rolesToAssign = unassignedRoles.stream().filter(role -> {
            return roleIds.contains(role.getId());
        }).collect(Collectors.toList());
        return rolesToAssign;
    }

    private List<RoleRepresentation> getRolesToRemove(KeycloakUser keycloakUser, String realm) {
        List<RoleRepresentation> assignedRoles = getAssignedRoleByUser(realm, keycloakUser.getKeyCloakUserId());
        List<String> defaultRoles = keycloak.realm(realm).toRepresentation().getDefaultRoles();
        List<String> roleIds = keycloakUser.getRoleIds();
        List<RoleRepresentation> rolesToRemove = assignedRoles.stream().filter(role -> {
            return !roleIds.contains(role.getId()) && !defaultRoles.contains(role.getName());
        }).collect(Collectors.toList());
        return rolesToRemove;
    }

    private UserResource getUserResourceByUserId(String realm, String userId) {
        return keycloakConfigResolverService.getKeycloak().realm(realm).users().get(userId);
    }

    private UserRepresentation getUserRepresentationByUserId(String realm, String userId) {
        return keycloakConfigResolverService.getKeycloak().realm(realm).users().get(userId).toRepresentation();
    }

    private UserRepresentation getUserRepresentation(KeycloakUser keycloakUser) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(keycloakUser.getUserName());
        user.setEmail(keycloakUser.getEmail());
        user.setEnabled(keycloakUser.isEnabled());
        return user;
    }

    private CredentialRepresentation getCredentials(KeycloakUser keycloakUser) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(keycloakUser.getPassword());
        credential.setTemporary(keycloakUser.isTemporary());
        return credential;
    }

    private String getCreatedUserId(ResponseEntity<Response> response) {
        HttpHeaders httpHeaders = response.getHeaders();
        List<String> list = httpHeaders.get("Location");
        if (list != null && list.size() > 0) {
            String userId = list.get(0).replaceAll(".*/(.*)$", "$1");
            return userId;
        } else {
            return "";
        }
    }

    private String getCreatedUserId(Response response) {
        String locationHeader = response.getHeaderString("Location");
        String userId = locationHeader.replaceAll(".*/(.*)$", "$1");
        return userId;
    }

    private KeycloakUser getKeyCloakUser(UserResource resource, String realm) {
        UserRepresentation representation = resource.toRepresentation();
        KeycloakUser keycloakUser = new KeycloakUser();
        keycloakUser.setEmail(representation.getEmail());
        keycloakUser.setEnabled(representation.isEnabled());
        keycloakUser.setKeyCloakUserId(representation.getId());
        keycloakUser.setPassword(null);
        List<RoleRepresentation> assignedRoles = getAssignedRoleByUser(realm, representation.getId());
        assignedRoles.stream().forEach(role -> {
            keycloakUser.getRoleIds().add(role.getId());
        });
        keycloakUser.setTemporary(false);
        keycloakUser.setUserName(representation.getUsername());
        return keycloakUser;
    }

    private List<RoleRepresentation> getAssignedRoleByUser(String realm, String userId) {
        UserResource userResource = getUserResourceByUserId(realm, userId);
        List<RoleRepresentation> assignedRoles = userResource.roles().realmLevel().listEffective();
        return assignedRoles;
    }

    /**
     * call keycloak to got token ,if got the token success return true, else return false
     *
     * @param userName user name
     * @param password user password
     * @return boolean
     */
    public boolean checkUserPassWord(String userName, String password) {
        log.info("call keycloak to got token for check user password...");
        try {
            TokenManager tokenManager = keycloakConfigResolverService.getKeycloak(userName, password).tokenManager();
            return tokenManager != null && !StringUtils.isEmpty(tokenManager.getAccessTokenString());
        } catch (Exception e) {
            log.error("check user [{}] password faild.", userName);
            return false;
        }
    }

    public boolean addUserToGroup(String userId, String groupId) {
        log.error("call keycloak to add user {} to user groups {}  ", userId, groupId);
        try {
            Response result = keycloakConfigResolverService.getKeycloak().realm(realm).user(userId).joinGroup(groupId);
            return result != null && HttpStatus.NO_CONTENT.value() == result.getStatus();
        } catch (KeyCloakServiceException kke) {
            log.error("call keycloak to add user {} to user groups {} error,error message is {} ", userId, groupId, kke.getErrorMessage());
        }
        return false;
    }

    public boolean leaveUserFromGroup(String userId, String groupId) {
        log.error("call keycloak to remove  user {} to user groups {}  ", userId, groupId);
        try {
            Response result = keycloakConfigResolverService.getKeycloak().realm(realm).user(userId).leaveGroup(groupId);
            return result != null && HttpStatus.NO_CONTENT.value() == result.getStatus();
        } catch (KeyCloakServiceException kke) {
            log.error("call keycloak to remove  user {} to user groups {} error,error message is {}  ", userId, groupId, kke.getErrorMessage());
        }
        return false;
    }

    /**
     * call keycloak to delete the user who's id is userId
     *
     * @param userId userId
     * @return
     */
    public boolean delete(String userId) {
        Response result = keycloakConfigResolverService.getKeycloak().realm(realm).users().delete(userId);
        if (HttpStatus.NO_CONTENT.value() != result.getStatus()) {
            LOGGER.info("Unable to delete user {}. Keycloak responded with status {}", userId, result.getStatus());
            throw new KeyCloakServiceException(
                    "Unable to create user. Keycloak responded with status " + result.getStatus());
        }
        return true;
    }
}