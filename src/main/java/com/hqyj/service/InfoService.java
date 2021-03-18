package com.hqyj.service;

import com.hqyj.pojo.Info;
import com.hqyj.pojo.UserInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public interface InfoService {
    //饼图
    HashMap<String,Object> bing(Info info);

    //柱状图
    HashMap<String,Object> zhu(Info info);

    //查询时间
    List<Info> selectTime();


}
