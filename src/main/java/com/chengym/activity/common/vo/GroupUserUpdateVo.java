package com.chengym.activity.common.vo;

import lombok.Data;

/**
 * description: params for group`s users add or move
 *
 * @author renhaixiang
 * @version 1.0
 * @date 2018/10/10 15:27
 **/
@Data
public class GroupUserUpdateVo extends GroupUserUpdateBaseVo {
    private String userIds;
}
