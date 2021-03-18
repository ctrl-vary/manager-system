package com.hqyj.controller;

import com.hqyj.pojo.UserInfo;
import com.hqyj.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    UserInfoService userInfoService;
    //访问User-list页面
    @RequestMapping("/list")
    public String list(UserInfo user, ModelMap m){

        HashMap<String, Object> map=userInfoService.select(user);

        //把数据传到前端
        m.put("info",map);
        //将查询条件回显
        m.put("vv",user.getConValue());
        return "user/user-list";
    }
    //处理查询表格的ajax数据
    @RequestMapping("/listAjax")
    @ResponseBody
    public HashMap<String,Object> listAjax(UserInfo user){

        return userInfoService.select(user);
    }

    //打开修改页面
        @RequestMapping("/editPage")
        public String editPage(UserInfo user, ModelMap m){
            //根据userId查询
            UserInfo u=userInfoService.selectByUserId(user);
            //把数据传到前端
            m.addAttribute("user",u);
            return "user/user-edit";
        }

        //处理修改的ajax请求
    @RequestMapping("/edit")
    @ResponseBody
    public HashMap<String,Object> edit(UserInfo user){
        HashMap<String,Object> map=new HashMap<String,Object>();
        String info=userInfoService.update(user);
        map.put("info",info);
        return map;
    }
    //处理删除的ajax请求
    @RequestMapping("/del")
    @ResponseBody
    public HashMap<String,Object> del(UserInfo user){
        HashMap<String,Object> map=new HashMap<String,Object>();
        String info=userInfoService.del(user);
        map.put("info",info);
        return map;
    }



}

