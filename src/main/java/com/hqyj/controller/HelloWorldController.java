package com.hqyj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // 加上controller注解的类 表示这个类 和前端页面进行交互
@RequestMapping("/hello") //定义 这个类的映射地址，指的是 浏览器访问这个类的地址
public class HelloWorldController {

    //定义一个访问helloworld页面的方法
    @RequestMapping("/test")
    public String test(){
        // return 的值 是 访问某个页面的前缀
        return "helloworld";
    }
}
