package com.hqyj.controller;

import com.hqyj.pojo.UserInfo;
import com.hqyj.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Controller
public class IndexController {
    @Autowired
    UserInfoService userInfoService;

    //获取图片上传的路径
    @Value("${file.address}")
    String fileAddress;

    //用户访问的图片路径
    @Value("${file.staticPath}")
    String upload;

    //访问 index页面
    @RequestMapping("/index")
    public String index(){

        return "index";
    }

    //访问 index页面
    @RequestMapping("/admin-list")
    public String admin(){
        return "admin-list";
    }

    //访问 客户地区统计页面
    @RequestMapping("/regionTj")
    public String rTj(){
        return "regionTj";
    }

    //访问用户个人资料页面
    @RequestMapping("/member-show")
    public String show(){
        return "member-show";
    }
    //访问用户个人资料页面
    @RequestMapping("/edit-password")
    public String epwd(){
        return "edit-password";
    }

    //访问 welcome页面
    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome";
    }
    //访问修改密码页面
    @RequestMapping("/updatePwd")
    public String updatePwd(){
        return "updatePwd";
    }

    //访问 修改头像页面
    @RequestMapping("/updateHead")
    public String updateHead(){
        return "updateHead";
    }

    //头像更改
    @RequestMapping("/saveHead")
    @ResponseBody
    public HashMap<String,Object> saveHead(UserInfo user,HttpServletRequest request){
        HashMap<String,Object> map=new HashMap<String,Object>();
        String info=userInfoService.updateHead(user,request);
        map.put("info",info);
        return map;
    }


    //处理获取登录用户的用户名
    @RequestMapping("/getUser")
    @ResponseBody
    public HashMap<String,Object> updatePwdAjax(HttpServletRequest request){
        HashMap<String,Object> map=new HashMap<String,Object>();
        //获取session中的用户名
        HttpSession session=request.getSession();
        UserInfo user=(UserInfo)session.getAttribute("user");
        map.put("info",user.getUserName());
        return map;
    }


    //处理修改密码的Ajax请求
    @RequestMapping("/updatePwdAjax")
    @ResponseBody
    public HashMap<String,Object> updatePwdAjax(UserInfo user, HttpServletRequest request){
        HashMap<String,Object> map=new HashMap<String,Object>();
        String info=userInfoService.updatePwd(user,request);
        map.put("info",info);
        return map;
    }

    //上传
    @RequestMapping("/upload")
    @ResponseBody
    public HashMap<String,Object> upload(MultipartFile file){
        HashMap<String,Object> map=new HashMap<String,Object>();
        //上传文件
        try {
            //定义一个文件上传目录
            String timeDir="";
            //获取时间
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
            timeDir=sdf.format(new Date());
            //定义文件上传前缀(为了保证文件上传存到服务器文件名是唯一的)
            String pre="";
            pre= UUID.randomUUID()+"";
            //获取文件的后缀名
            String hou="";
            if(file!=null){
                //.jpg
                String originalName = file.getOriginalFilename();
                hou=originalName.substring(originalName.lastIndexOf(".")+1);
            }
            //定义文件上传的全路径
            String filePath=fileAddress+"\\"+timeDir+"\\"+pre+"."+hou;
            //创建file对象
            File f=new File(filePath);
            //判断目录是否存在，不存在则创建目录
            if(!f.isDirectory()){
                //创建目录
                f.mkdirs();
            }
            //上传文件
            file.transferTo(f);
            //设置上传成功的提示信息
            map.put("code",0);
            //返回给前端 用户访问这个图片的路径
            String path=upload+"\\"+timeDir+"\\"+pre+"."+hou;
            map.put("src",path);
            return map;

        } catch (IOException e) {
            e.printStackTrace();
        }
        //设置失败成功的提示信息
        map.put("code",1);
        return map;
    }







}








