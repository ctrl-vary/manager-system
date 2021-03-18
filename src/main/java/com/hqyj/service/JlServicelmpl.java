package com.hqyj.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hqyj.dao.JlDao;
import com.hqyj.pojo.jl;
import com.hqyj.pojo.kh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class JlServicelmpl implements JlService{
    //创建一个userInfoDao的实现类对象
    @Autowired(required = false)
    JlDao jlDao;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public String add(jl jl) {
        int n = jlDao.addJl(jl);
        if(n>0){
            return "添加成功";
        }
        return "添加失败";
    }

    @Override
    public HashMap<String, Object> select(jl jl) {

        HashMap<String, Object> map=new HashMap<String, Object>();
        //设置分页参数
        PageHelper.startPage(jl.getPage(),jl.getRow());

        List<jl> list=new ArrayList<>();

        //判断用户输入的查询条件是否有值
        if(jl.getConValue().equals("")){
            //查询数据库
            list=jlDao.select();

        }else{
            //根据用户选择查询条件判断用户需要查询
           if(jl.getCondition().equals("客户经理编号")){
                jl.setManagerid(Integer.parseInt(jl.getConValue()));
                list=jlDao.findByManagerId(jl);
            }else if(jl.getCondition().equals("客户经理姓名")){
                jl.setManager(jl.getConValue());
                list=jlDao.findByManager(jl);
            }else {
                list=jlDao.select();
            }
        }
        //把查询的数据转换成分页对象
        PageInfo<jl> page = new PageInfo<jl>(list);
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
    public jl selectById(jl jl) {

            return jlDao.selectByManagerId(jl);
    }

    @Override
    public String update(jl jl) {
        System.out.println("editservicelmpl---------------------------------------");

        int num = jlDao.update(jl);
        System.out.println(num+"num?---------------------------------------");
        if (num > 0) {
            System.out.println("num>0---------------------------------------");
            return "修改成功";
        }
        return "修改失败";
    }

}
