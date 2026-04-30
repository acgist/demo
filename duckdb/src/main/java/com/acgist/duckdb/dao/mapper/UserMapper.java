package com.acgist.duckdb.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.acgist.duckdb.data.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    List<User> selectAll();
    
    void deleteById(List<Long> id);

}
