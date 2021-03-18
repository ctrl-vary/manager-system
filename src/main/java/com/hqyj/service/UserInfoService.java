package com.hqyj.service;

import com.hqyj.pojo.UserInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public interface UserInfoService {

    //登录方法
    String login(UserInfo user,HttpServletRequest request);

    //注册
    String zhuce(UserInfo user);

    //邮件发送
    HashMap<String,Object> sendCode(UserInfo user, HttpServletRequest request);

    //发送验证码
    HashMap<String,Object> getCode(UserInfo user, HttpServletRequest request);

    //查询
    HashMap<String,Object> select(UserInfo user);

    //根据userId查询
    UserInfo selectByUserId(UserInfo user);

    //修改
    String update(UserInfo user);

    //删除
    String del(UserInfo user);

    //修改密码
    String updatePwd(UserInfo user,HttpServletRequest request);

    //修改头像
    String updateHead(UserInfo user, HttpServletRequest request);
}








