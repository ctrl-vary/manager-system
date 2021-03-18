package com.hqyj.service;

import com.hqyj.pojo.jl;
import com.hqyj.pojo.kh;

import java.util.HashMap;

public interface JlService {
    //添加
    String add(jl jl);

    //查询
    HashMap<String,Object> select(jl jl);

    //根据Id查询
    jl selectById(jl jl);

    //修改
    String update(jl jl);
}
