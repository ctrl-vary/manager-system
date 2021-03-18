package com.hqyj.controller;
import com.hqyj.pojo.jl;
import com.hqyj.pojo.kh;
import com.hqyj.service.JlService;
import com.hqyj.service.KhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class JlController {


    @Autowired
    JlService jlService;


    //访问 添加客户列表页面页面
    @RequestMapping("/jl-add")
    public String jladd(){
        return "jl-add";
    }

    //访问 客户列表页面页面
    @RequestMapping("/jl-list")
    public String jlview(jl jl, ModelMap m){
        HashMap<String, Object> map=jlService.select(jl);
        //把数据传到前端
        m.put("info",map);
        //将查询条件回显
        m.put("vv",jl.getConValue());
        return "jl-list";
    }
    //访问 编辑客户列表页面页面
    @RequestMapping("/jl-edit")
    public String jledit(jl jl, ModelMap m){
        System.out.println(jl.getManagerid());
        //根据userId查询
        jl jl1=jlService.selectById(jl);
        System.out.println(jl.getUrl()+"---------------------");
        //把数据传到前端
        m.addAttribute("jl",jl1);
        return "jl-edit";
    }

    @RequestMapping("/edit1")
    @ResponseBody
    public HashMap<String,Object> edit(jl jl){
        System.out.println("controller---------------------------------------");
        HashMap<String,Object> map=new HashMap<String,Object>();
        String info=jlService.update(jl);
        System.out.println(info+"info---------------------------------------");
        map.put("info",info);
        return map;
    }
    //处理添加客户请求
    @RequestMapping("/addJl")
    @ResponseBody
    public HashMap<String,Object> addJl(jl jl){
        HashMap<String,Object> map = new HashMap<String,Object>();
        //访问注册方法
        String info = jlService.add(jl);
        map.put("info",info);
        return map;
    }

}
