package com.chengym.activity.common.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * description: params for group`s users add or move
 *
 * @author renhaixiang
 * @version 1.0
 * @date 2018/10/10 15:27
 **/
@Data
public class GroupUsersUpdateVo extends GroupUserUpdateBaseVo{
    /**
     * description: userId: user_base_info`s id for user the only one flag
     **/
    @NotNull
    private String addUsers;
    @NotNull
    private String removeUsers;

}
