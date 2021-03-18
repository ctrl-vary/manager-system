package com.hqyj.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hqyj.dao.UserInfoDao;
import com.hqyj.pojo.UserInfo;
import com.hqyj.util.MdFive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service  //UserInfoServiceImpl的对象创建交给spring管理
public class UserInfoServiceImpl implements UserInfoService {
    //创建一个userInfoDao的实现类对象
    @Autowired(required = false)
    UserInfoDao userInfoDao;

    //创建加密工具类对象
    @Autowired
    MdFive mdfive;

    //获取发件人邮箱
    @Value("${spring.mail.username}")
    String sendEmail;

    //创建发送邮件的对象
    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;


    /**
     * 检查用户是·否已经被锁定，如果是，返回剩余锁定时间，如果否，返回-1
     * @param username  username
     * @return  时间
     */
    private long getUserLoginTimeLock(String username) {
        String key = "user:" + username + ":lockTime";
        //获取key 过期时间（剩余时间）
        long lockTime = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        if(lockTime > 0){//查询用户是否已经被锁定，如果是，返回剩余锁定时间，如果否，返回-1
            return lockTime;
        }else{
            return -1;
        }
    }
    /**
     * 获取当前用户已失败次数
     * @param username  username
     * @return  已失败次数
     */
    private int getUserFailCount(String username){
        String key = "user:" + username + ":failCount";
        //从redis中获取当前用户已失败次数
        Object object = redisTemplate.opsForValue().get(key);
        if(object != null){
            return (int)object;
        }else{
            return -1;
        }
    }
    /**
     * 设置失败次数
     * @param username  username
     */
    private void setFailCount(String username){
        //获取当前用户已失败次数
        long count = this.getUserFailCount(username);
        String key = "user:" + username + ":failCount";
        if(count < 0){//判断redis中是否有该用户的失败登陆次数，如果没有，设置为1，过期时间为1分钟，如果有，则次数+1
            redisTemplate.opsForValue().set(key,1,60,TimeUnit.SECONDS);
        }else{
            redisTemplate.opsForValue().increment(key,new Double(1));
        }
    }

    @Override
    public String login(UserInfo user,HttpServletRequest request) {
        //先判断用户是否被锁定
        if(this.getUserLoginTimeLock(user.getUserName())<0){
            //查询用户名是否存在,如果存在就取出其盐值
            UserInfo u=userInfoDao.selectByName(user);
            if(u!=null){
                //加密用户输入的密码
                String pwd = mdfive.encrypt(user.getUserPwd(),u.getSalt());
                //把加过密码的传到数据层中
                user.setUserPwd(pwd);
                //查询数据层的登录方法，并且拿到返回值
                UserInfo userinfo =userInfoDao.login(user);
                //如果查询到值，userinfo就不等于null，否则就等于null
                if(userinfo!=null){
                    //创建session对象
                    HttpSession session = request.getSession();
                    //存用户对象
                    session.setAttribute("user",userinfo);
                    return "登录成功";
                }else{//密码输入错误;
                    //设置密码输入失败次数
                    setFailCount(user.getUserName());
                    //获取失败次数
                    int num=getUserFailCount(user.getUserName());
                    //定义允许用户失败次数
                    int cc=3;
                    //剩余多少次
                    int c=cc-num;
                    if(c>0){
                        return "输入密码错误"+num+"次，还剩余"+c+"次";
                    }else{
                        String key = "user:" + user.getUserName() + ":lockTime";
                        //设置失效时间
                        redisTemplate.opsForValue().set(key,222,60,TimeUnit.SECONDS);
                        return "您输入错误密码次数超过"+cc+"次，账号已被锁定";
                    }
                }
            }else{
                return "用户名输入错误";
            }
        }else {
            return "该用户账号已被锁定，请一分钟后再尝试";
        }

//        //查询用户名是否存在,如果存在就取出其盐值
//        UserInfo u=userInfoDao.selectByName(user);
//
//        if(u!=null){
//            //加密用户输入的密码
//            String pwd = mdfive.encrypt(user.getUserPwd(),u.getSalt());
//            //把加过密码的传到数据层中
//            user.setUserPwd(pwd);
//            //查询数据层的登录方法，并且拿到返回值
//            UserInfo userinfo =userInfoDao.login(user);
//            //如果查询到值，userinfo就不等于null，否则就等于null
//            if(userinfo!=null){
//                //创建session对象
//                HttpSession session = request.getSession();
//                //存用户对象
//                session.setAttribute("user",userinfo);
//                return "登录成功";
//            }
//        }else{
//            return "用户名输入错误";
//        }
//
//
//
//        return "登录失败";
    }

    @Override
    public String zhuce(UserInfo user) {
        //查询用户名是否重名
        int num = userInfoDao.valName(user);
//        int n0=userInfoDao.valEmail(user);
        if(num>0){
            return "该用户名已经被注册";
        }
//        else if(n0>0) {
//            return "该邮箱已被绑定";
//        }
        else{
            //自动生成一个盐值
            Random rd=new Random();
            String salt=rd.nextInt(10000)+"";

            //加密用户输入的密码
            String pwd = mdfive.encrypt(user.getUserPwd(),salt);
            //把加过密码的传到数据层中
            user.setUserPwd(pwd);
            //存入盐值
            user.setSalt(salt);
            //注册
            int n = userInfoDao.zhuce(user);
            if(n>0){
                return "注册成功";
            }
        }
        return "注册失败";
    }


    @Override
    public HashMap<String, Object> sendCode(UserInfo user, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(userInfoDao.valEmail(user)<=0){
            map.put("info","该邮箱未被绑定!");
            return map;
        }
        try {
            //从session中获取当前用户信息
            HttpSession session = request.getSession();
            //创建普通邮件对象
            SimpleMailMessage message = new SimpleMailMessage();
            //设置发件人邮箱
            message.setFrom(sendEmail);
            //设置收件人邮箱
            message.setTo(user.getEmail());
            //设置邮件标题
            message.setSubject("学员信息管理系统验证码");
            // 生成随机验证码
            Random rd = new Random();
            String valCode = rd.nextInt(9999)+"";
            //设置邮件正文
            message.setText("你的验证码是:"+valCode);
            //发送邮件
            javaMailSender.send(message);
            //发送成功
            //把验证码存入session中
            session.setAttribute("valCode",valCode);
            session.setAttribute("name",userInfoDao.selectByEmail(user));
            map.put("info","发送成功");
            return map;

        } catch (Exception e) {
            System.out.println("发送邮件时发生异常！");
            e.printStackTrace();
        }
        map.put("info","发送邮件异常");
        return map;
    }


    @Override
    public HashMap<String, Object> getCode(UserInfo user, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(userInfoDao.valEmail(user)>0){
            map.put("info","该邮箱已被绑定！");
            return map;
        }
        try {
            //从session中获取当前用户信息
            HttpSession session = request.getSession();
            //创建普通邮件对象
            SimpleMailMessage message = new SimpleMailMessage();
            //设置发件人邮箱
            message.setFrom(sendEmail);
            //设置收件人邮箱
            message.setTo(user.getEmail());
            //设置邮件标题
            message.setSubject("桔子管理系统验证码");
            // 生成随机验证码
            Random rd = new Random();
            String valCode = rd.nextInt(9999)+"";
            //设置邮件正文
            message.setText("你的验证码是:"+valCode);
            //发送邮件
            javaMailSender.send(message);
            //发送成功
            //把验证码存入session中
            session.setAttribute("valCode",valCode);
            map.put("name",userInfoDao.selectByEmail(user));
            map.put("info","发送成功");
            return map;

        } catch (Exception e) {
            System.out.println("发送邮件时发生异常！");
            e.printStackTrace();
        }
        map.put("info","发送邮件异常");
        return map;
    }

    @Override
    public HashMap<String, Object> select(UserInfo user) {
        HashMap<String, Object> map=new HashMap<String, Object>();
        //设置分页参数
        PageHelper.startPage(user.getPage(),user.getRow());

        List<UserInfo> list=new ArrayList<>();

        //判断用户输入的查询条件是否有值
        if(user.getConValue().equals("")){
            //判断缓存中是否有值
            String key="wen";
            Object obj=redisTemplate.opsForValue().get(key);
            if(obj==null){
                //查询数据库
                list=userInfoDao.select();
                //存入缓存
                redisTemplate.opsForValue().set(key,list,10,TimeUnit.MINUTES);

            }else {
                //读取缓存数据
                list=(List<UserInfo>)obj;
            }
           // list=userInfoDao.select();
        }else{
            //根据用户选择查询条件判断用户需要查询
            if(user.getCondition().equals("编号")){
                //设置用户输入的查询条件
                user.setUserId(Integer.parseInt(user.getConValue()));
                list=userInfoDao.findByUserId(user);
            }else if(user.getCondition().equals("用户名")){

                user.setUserName(user.getConValue());
                list=userInfoDao.findByUserName(user);
            }else {
                list=userInfoDao.select();
            }
        }
        //把查询的数据转换成分页对象
        PageInfo<UserInfo> page = new PageInfo<UserInfo>(list);
        //获取分页的当前页集合
        map.put("list",page.getList());
        //获取总条数
        map.put("total",page.getTotal());
        //总页数
        map.put("totalPage",page.getPages());
        //上一页
        if(page.getPrePage()==0){
            map.put("pre",1);
        }else{
            map.put("pre",page.getPrePage());
        }
        //下一页
        //保持在最后一页
        if(page.getNextPage()==0){
            map.put("next",page.getPages());
        }else{
            map.put("next",page.getNextPage());
        }
        //当前页
        map.put("cur",page.getPageNum());
        return map;
    }

    @Override
    public UserInfo selectByUserId(UserInfo user) {
        return userInfoDao.selectByUserId(user);
    }

    @Override
    public String update(UserInfo user) {

        //验证修改的用户名是否重名
        int v = userInfoDao.valName(user);
        if (v == 0) {
            int num = userInfoDao.update(user);
            if (num > 0) {
                //更新缓存
                String key="wen";
                //查询数据库
               List<UserInfo> list=userInfoDao.select();
                //存入缓存
                redisTemplate.opsForValue().set(key,list,10,TimeUnit.MINUTES);

                return "修改成功";
            }
        }else {
          return "用户名重名";
        }


        return "修改失败";
    }

    @Override
    public String del(UserInfo user) {
        int num=userInfoDao.del(user);
        if(num>0){
            return "删除成功";
        }
        return "删除失败";
    }

    @Override
    public String updatePwd(UserInfo user,HttpServletRequest request) {
        //取出存入session中的密码
        HttpSession session=request.getSession();
        UserInfo u=(UserInfo) session.getAttribute("user");
        String pwd=u.getUserPwd();
        String oldpwd=mdfive.encrypt(user.getOldPwd(),u.getSalt());
        //判断用户输入的密码是否正确
        if(oldpwd.equals(pwd)){

            //加密新密码
            String p=mdfive.encrypt(user.getNewPwd(),u.getSalt());
            //存入加密后的新密码
            u.setUserPwd(p);
            int num=userInfoDao.updatePwd(u);
            if(num>0){
                return "修改密码成功";
            }
        }else{
            return "旧密码输入不正确";
        }
        return "修改密码失败";
    }

    @Override
    public String updateHead(UserInfo user, HttpServletRequest request) {
        HttpSession session=request.getSession();
        UserInfo u=(UserInfo) session.getAttribute("user");
        u.setUrl(user.getUrl());
        //存入id
        user.setUserId(u.getUserId());

        int num=userInfoDao.updateHead(user);
        if(num>0){
            return "保存成功";
        }
          return "保存失败";
    }

}
