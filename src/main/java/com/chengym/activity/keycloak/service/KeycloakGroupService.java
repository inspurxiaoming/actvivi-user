package com.chengym.activity.keycloak.service;

import com.chengym.active.common.OwnRestTemplate;
import com.chengym.active.common.bean.User;
import com.chengym.active.common.exception.ApiException;
import com.chengym.active.common.vo.GroupVo;
import com.chengym.active.keycloak.Keycloak;
import com.chengym.active.keycloak.config.ConfigProperties;
import com.chengym.active.keycloak.exception.KeyCloakServiceException;
import com.chengym.active.keycloak.model.KeycloakUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.List;

@Component
@Slf4j
public class KeycloakGroupService {

    public static final Logger LOGGER = LoggerFactory.getLogger(KeycloakGroupService.class);

    @Autowired
    private Keycloak keycloak;
    @Autowired
    private ConfigProperties configProperties;
    @Autowired
    KeycloakConfigResolverService keycloakConfigResolverService;

    @Autowired
    @Qualifier("ownRestTemplate")
    OwnRestTemplate ownRestTemplate;

    /**
     * Call keycloak's interface to create Group
     * If successful, groupId is returned, otherwise "" is returned.
     *
     * @param groupVo
     * @param realm
     * @param user
     * @return
     */
    public String create(GroupVo groupVo, String realm, User user) {
        GroupRepresentation group = getGroupRepresentation(groupVo, user);
        //call keycloak to create Group
        String token = keycloakConfigResolverService.getKeycloak().tokenManager().getAccessTokenString();
        log.info("getAccessTokenString {}", token);
        Response result = keycloakConfigResolverService.getKeycloak().realm(realm).groups().add(group);
        if (result.getStatus() != HttpStatus.CREATED.value()) {
            LOGGER.info("Unable to create userGroup. Keycloak responded with status {}", result.getStatus());
            if(result.getStatus() == HttpStatus.CONFLICT.value()){
                throw new ApiException(String.valueOf(result.getStatus()),"您的用户组中存在此名称用户组");
            }else{
                throw new KeyCloakServiceException(
                        "Unable to create userGroup. Keycloak responded with status " + result.getStatus());
            }
        }
        String groupId = getCreatedGroupId(result);
        return groupId;
    }

    public List<GroupRepresentation> getGroups(String realm) {
        return keycloak.realm(realm).groups().groups();
    }

    private GroupRepresentation getGroupRepresentation(GroupVo groupVo, User user) {
        GroupRepresentation group = new GroupRepresentation();
        group.setName(user.getId() + "-" + groupVo.getGroupName());
        group.setId(groupVo.getGroupId());
        return group;
    }

    private CredentialRepresentation getCredentials(KeycloakUser keycloakUser) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(keycloakUser.getPassword());
        credential.setTemporary(keycloakUser.isTemporary());
        return credential;
    }

    /**
     * Get the groupId from Response
     *
     * @param response
     * @return
     */
    private String getCreatedGroupId(Response response) {
        String locationHeader = response.getHeaderString("Location");
        log.info("创建用户组的头信息{}",locationHeader);
        locationHeader.replaceAll(".*/(.*)$", "$1");
        log.info("替换后用户组信息groupId{}",locationHeader.replaceAll(".*/(.*)$", "$1"));
        String groupId = StringUtils.isEmpty(locationHeader) ? "" : locationHeader.replaceAll(".*/(.*)$", "$1");
        return groupId;
    }

    public static void main(String[] args) {
        String Str = new String("www.google.com");
        System.out.print("匹配成功返回值 :");
        System.out.println(Str.replaceAll("(.*)google(.*)", "runoob"));
        System.out.println(Str.replaceAll("w*","ttt"));
        System.out.print("匹配失败返回值 :");
        System.out.println(Str.replaceAll("(.*)taobao(.*)", "runoob"));
        System.out.println(Str);

        String Str1 = "www.google.com";
        System.out.print("匹配成功返回值 :");
        System.out.println(Str1.replaceAll("(.*)google(.*)", "runoob"));
        System.out.println(Str1);
        System.out.print("匹配失败返回值 :");
        System.out.println(Str1.replaceAll("(.*)taobao(.*)", "runoob"));
        System.out.println(Str1);

        String a = "2012.02.20";
        System.out.println("--1--->"+a+"----->");
        a = a.replaceAll("\\.", "");
        System.out.println("--2--->"+a+"----->");
    }

    /**
     * Calling keycloak's interface to delete the group ,that id is groupVo.groupId .
     * If successful, true is returned, otherwise false is returned.
     *
     * @param groupVo
     * @param realm
     * @param user
     * @return
     */
    public boolean delete(GroupVo groupVo, String realm, User user) {

        GroupRepresentation group = getGroupRepresentation(groupVo, user);
        try {
            Response result = keycloakConfigResolverService.getKeycloak().realm(realm).groups().group(group.getId()).remove();
            if (HttpStatus.NO_CONTENT.value() == result.getStatus()) {
                return true;
            } else {
                return false;
            }
        } catch (KeyCloakServiceException kke) {
            log.error("call keycloak to delete group {} id is {}, error ,error message is {} ", group.getName(), group.getId(), kke.getErrorMessage());
            return false;
        }

    }
}