package com.chengym.activity.common.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description: params for group`s users add or move
 *
 * @author renhaixiang
 * @version 1.0
 * @date 2018/10/10 15:27
 **/
@Data
public class GroupVo {
    /**
     * description: groupName: user_groups`s group_name for group
     **/
    @NotNull
    private String groupName;
    /**
     * description: groupDesc: user_groups`s group_desc for group
     **/
    @NotBlank
    private String groupDesc;
    /**
     * description: groupId:user_groups's id for group (delete or other operate)
     */
    private String groupId;

}
