package com.hqyj.dao;
import com.hqyj.pojo.jl;
import com.hqyj.pojo.kh;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
@Mapper
public interface JlDao {
    //添加经理
    @Insert("insert into jl (manager,gender,url,idn,bdate,phone,email,edu,tips) value (#{manager},#{gender},#{url},#{idn},#{bdate},#{phone},#{email},#{edu},#{tips})")
    int addJl(jl jl);

    //分页查询
    @Select("select * from jl")
    List<jl> select();

    //根据编号查询
    @Select("select * from jl where managerid=#{managerid}")
    List<jl> findByManagerId(jl jl);

    //根据姓名查询
    @Select("select * from jl where manager=#{manager}")
    List<jl> findByManager(jl jl);

    //根据Id查询
    @Select("select * from jl where managerid=#{managerid}")
    jl selectByManagerId(jl jl);

    //修改
    @Update("update jl set manager=#{manager},gender=#{gender},url=#{url},idn=#{idn},bdate=#{bdate},phone=#{phone},email=#{email},edu=#{edu},tips=#{tips} where managerid=#{managerid}")
    int update(jl jl);
}
