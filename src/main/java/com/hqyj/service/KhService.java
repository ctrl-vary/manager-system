package com.hqyj.service;

import com.hqyj.pojo.UserInfo;
import com.hqyj.pojo.kh;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public interface KhService {
    //添加
    String add(kh kh);

    //查询
    HashMap<String,Object> select(kh kh);

    //根据Id查询
    kh selectById(kh kh);

    //修改
    String update(kh kh);


}
