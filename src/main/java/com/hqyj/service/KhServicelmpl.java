package com.hqyj.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hqyj.dao.KhDao;
import com.hqyj.pojo.UserInfo;
import com.hqyj.pojo.kh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class KhServicelmpl implements KhService {
    //创建一个userInfoDao的实现类对象
    @Autowired(required = false)
    KhDao khDao;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public String add(com.hqyj.pojo.kh kh) {
        int n = khDao.addKh(kh);
        if(n>0){
            return "添加成功";
        }
        return "添加失败";
    }

    @Override
    public HashMap<String, Object> select(kh kh) {

        HashMap<String, Object> map=new HashMap<String, Object>();
        //设置分页参数
        PageHelper.startPage(kh.getPage(),kh.getRow());

        List<kh> list=new ArrayList<>();

        //判断用户输入的查询条件是否有值
        if(kh.getConValue().equals("")){
                //查询数据库
                list=khDao.select();

        }else{
            //根据用户选择查询条件判断用户需要查询
            if(kh.getCondition().equals("客户编号")){
                //设置用户输入的查询条件
                kh.setId(Integer.parseInt(kh.getConValue()));
                list=khDao.findById(kh);
            }else if(kh.getCondition().equals("客户姓名")){
                kh.setName(kh.getConValue());
                list=khDao.findByName(kh);
            }else if(kh.getCondition().equals("客户经理编号")){
                kh.setManagerid(Integer.parseInt(kh.getConValue()));
                list=khDao.findByManagerId(kh);
            }else if(kh.getCondition().equals("客户经理姓名")){
                kh.setManager(kh.getConValue());
                list=khDao.findByManager(kh);
            }else {
                list=khDao.select();
            }
        }
        //把查询的数据转换成分页对象
        PageInfo<kh> page = new PageInfo<kh>(list);
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
    public kh selectById(kh kh) {
        return khDao.selectById(kh);
    }

    @Override
    public String update(kh kh) {
        System.out.println("editservice---------------------------------------");

            int num = khDao.update(kh);
        System.out.println(num+"---------------------------------------");
            if (num > 0) {
                System.out.println("editservicecg---------------------------------------");
                return "修改成功";
            }
        return "修改失败";
    }



}
