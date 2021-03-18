package com.hqyj.service;

import com.hqyj.pojo.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserInfoServiceImplTest {
    //操作redis库的操作对象
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    //测试ridis数据获取
    @Test
    public  void add(){
        //设置失效时间值
        //(参数：键名，值，时间，单位)
        redisTemplate.opsForValue().set("name","张三",30, TimeUnit.SECONDS);
        //获取失效时间
        Long time=redisTemplate.getExpire("name");
        System.out.println("失效时间="+time);
        //获取键名的剩余时间
        Long Mytime=redisTemplate.getExpire("name",TimeUnit.SECONDS);
        System.out.println("剩余时间="+Mytime);
        //设置值
        redisTemplate.opsForValue().set("name","张三");
        //取值
        String name =redisTemplate.opsForValue().get("name")+"";
        System.out.println("name="+name);
        //插入一个userinfo对象到redis库中
        UserInfo user=new UserInfo();
        user.setUserId(3);
        user.setUserName("xz");
        user.setUserPwd("0813");
        redisTemplate.opsForValue().set("user",user);
        //取值
        UserInfo userInfo=(UserInfo)redisTemplate.opsForValue().get("user");
        System.out.println("id="+userInfo.getUserId());
        System.out.println("name="+userInfo.getUserName());
        System.out.println("pwd="+userInfo.getUserPwd());
    }
    @Test
    public void del(){
        redisTemplate.delete("arc");
    }
}