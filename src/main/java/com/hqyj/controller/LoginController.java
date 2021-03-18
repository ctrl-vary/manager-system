package com.hqyj.controller;

import com.hqyj.pojo.UserInfo;
import com.hqyj.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@RequestMapping("/login")
public class LoginController {


    //创建一个userInfoService的实现类对象
    @Autowired
    UserInfoService userInfoService;

    //接收用户发送的登录信息，用户名和密码
    //ModelMap 是用来把服务端的值传给前端的
    @RequestMapping("/loginForm")
    public String loginForm(UserInfo user, ModelMap map,HttpServletRequest request){

        String info = userInfoService.login(user,request);
        map.addAttribute("info",info);
        //返回登录页面
        return "login";
    }
    //访问登录页面login.html
    @RequestMapping("/loginPage")
    public String loginPage(){
        return "login";
    }

    //ajax登录
    @RequestMapping("/loginAjax")
    @ResponseBody
    public HashMap<String,Object> loginajax(UserInfo user,HttpServletRequest request){
        HashMap<String,Object> map = new HashMap<String,Object>();
        String info = userInfoService.login(user,request);
        map.put("info",info);

        return map;
    }
    //访问注册页面zhuce.html
    @RequestMapping("/zhucePage")
    public String zhucePage(){
        return "zhuce";
    }

    //处理注册请求
    @RequestMapping("/zhuce")
    @ResponseBody
    public HashMap<String,Object> zhuce(UserInfo user){
        HashMap<String,Object> map = new HashMap<String,Object>();
        //访问注册方法
        String info = userInfoService.zhuce(user);
        map.put("info",info);
        return map;
    }

    //处理邮件发送的请求
    @RequestMapping("/sendEmail")
    @ResponseBody
    public HashMap<String,Object> sendEmail(UserInfo user, HttpServletRequest request){
        return userInfoService.sendCode(user,request);
    }

    //获取验证码
    @RequestMapping("/getCode")
    @ResponseBody
    public HashMap<String,Object> getCode(UserInfo user, HttpServletRequest request){
        return userInfoService.getCode(user,request);
    }

    //处理邮件登录的请求
    @RequestMapping("/emailLogin")
    @ResponseBody
    public HashMap<String,Object> emailLogin(String code,HttpServletRequest request){
        HashMap<String,Object> map = new HashMap<String,Object>();

        //获取session对象
        HttpSession session = request.getSession();
        //取出存到session中的验证码的值
        String valCode = session.getAttribute("valCode")+"";
        //判断用户输入的验证码和邮箱接收的验证码是否一致
        if(code.equals(valCode)){
            map.put("info","邮箱登录成功");
        }else{
            map.put("info","验证码输入错误");
        }
        return map;

    }

    //处理邮件bd的请求
    @RequestMapping("/emailBd")
    @ResponseBody
    public HashMap<String,Object> emailBd(String code,HttpServletRequest request){
        HashMap<String,Object> map = new HashMap<String,Object>();

        //获取session对象
        HttpSession session = request.getSession();
        //取出存到session中的验证码的值
        String valCode = session.getAttribute("valCode")+"";
        //判断用户输入的验证码和邮箱接收的验证码是否一致
        if(code.equals(valCode)){
            map.put("info","邮箱绑定成功");
        }else{
            map.put("info","验证码输入错误");
        }
        return map;

    }



}
