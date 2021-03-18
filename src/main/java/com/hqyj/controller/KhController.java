package com.hqyj.controller;
import com.hqyj.pojo.UserInfo;
import com.hqyj.pojo.kh;
import com.hqyj.service.KhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Console;
import java.util.HashMap;

@Controller
public class KhController {
    @Autowired
    KhService khService;

    //访问 添加客户列表页面页面
    @RequestMapping("/cate-add")
    public String cateadd(){
        return "cate-add";
    }

   

    //访问 编辑客户列表页面页面
    @RequestMapping("/cate-edit")
    public String cateedit(kh kh, ModelMap m){
        System.out.println(kh.getId());
        //根据userId查询
        kh kh1=khService.selectById(kh);
        System.out.println(kh1.getUrl()+"---------------------");
        //把数据传到前端
        m.addAttribute("kh",kh1);
        return "cate-edit";
    }

    @RequestMapping("/edit")
    @ResponseBody
    public HashMap<String,Object> edit(kh kh){
        System.out.println("edit---------------------------------------");
        HashMap<String,Object> map=new HashMap<String,Object>();
        String info=khService.update(kh);
        System.out.println(info+"---------------------------------------");
        map.put("info",info);
        return map;
    }

    //访问 客户列表页面页面
    @RequestMapping("/cate-list")
    public String kh(kh kh, ModelMap m){
        HashMap<String, Object> map=khService.select(kh);
        //把数据传到前端
        m.put("info",map);
        //将查询条件回显
        m.put("vv",kh.getConValue());
        return "cate-list";
    }

    //处理添加客户请求
    @RequestMapping("/addKh")
    @ResponseBody
    public HashMap<String,Object> addKh(kh kh){
        HashMap<String,Object> map = new HashMap<String,Object>();
        //访问注册方法
        String info = khService.add(kh);
        map.put("info",info);
        return map;
    }
}