package com.chengym.activity.testUser.controller;

import com.chengym.active.SecurityContextUtil;
import com.chengym.active.common.bean.ResponseBean;
import com.chengym.active.common.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * desc:
 *
 * @param
 * @author chengym
 * @return
 * @date 2019/07/20 12:15
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("")
    public ResponseBean get(){
        ResponseBean responseBean = new ResponseBean();
        User user = SecurityContextUtil.getLoginUser();
        responseBean.setResult(user);
        return responseBean;
    }
}
