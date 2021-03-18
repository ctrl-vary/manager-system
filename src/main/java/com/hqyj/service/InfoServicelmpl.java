package com.hqyj.service;

import com.hqyj.dao.InfoDao;
import com.hqyj.pojo.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class InfoServicelmpl implements InfoService {

    @Autowired(required = false)
    InfoDao infoDao;
    @Override
    public HashMap<String, Object> bing(Info info) {
        HashMap<String, Object> map =new HashMap<String, Object>();
        //查询数据库
        List<Info> list=infoDao.select(info);
        //构建饼图需要的数据类型
        List<HashMap<String, Object>> mapList=new ArrayList<>();
        //遍历list
        for(Info i:list){
            HashMap<String,Object> m=new HashMap<>();
            //判断用户统计的查询数据
            if(info.getCon().equals("0")){
              //加载确证人数
                m.put("value",i.getConfirmCount());

            }else if(info.getCon().equals("1")){
                m.put("value",i.getCuredCount());
            }else{
                m.put("value",i.getDeadCount());
            }

            m.put("name",i.getProvinceName());
            //添加到构建饼图需要的集合
            mapList.add(m);
        }
        //把构建好的饼图数据存到map中
        map.put("info",mapList);
        return map;
    }

    @Override
    public HashMap<String, Object> zhu(Info info) {
        HashMap<String, Object> map=new HashMap<String, Object>();
        //查询数据库
        List<Info> list=infoDao.select(info);
        //构建柱状图需要的数据
        List<String> xlist=new ArrayList<>();
        List<Integer> ylist=new ArrayList<>();

        for(Info i:list){
            if(info.getCon().equals("0")){
               ylist.add(i.getConfirmCount());

            }else if(info.getCon().equals("1")){
                ylist.add(i.getCuredCount());
            }else{
                ylist.add(i.getDeadCount());
            }
            //加载x轴数据
            xlist.add(i.getProvinceName());
        }
        //把x轴数据装到map中
        map.put("x",xlist);
        //把y轴数据装到map中
        map.put("y",ylist);
        return map;
    }

    @Override
    public List<Info> selectTime() {
        return infoDao.selectTime();
    }
}












