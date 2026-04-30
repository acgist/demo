package com.acgist.duckdb.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acgist.duckdb.dao.mapper.RelMapper;
import com.acgist.duckdb.dao.mapper.UserMapper;
import com.acgist.duckdb.data.entity.User;
import com.acgist.duckdb.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RelMapper relMapper;
    private final UserMapper userMapper;
    
    @Override
    @Transactional
    public void rel() {
        this.relMapper.deleteById(1L);
        this.relMapper.deleteById(2L);
        this.relMapper.insert(1L, List.of(1L, 2L));
        this.relMapper.insert(2L, List.of(3L, 4L));
    }
    
    @Override
    @Transactional
    public void user() {
//      this.userMapper.deleteById(1L);
//      this.userMapper.deleteById(2L);
        this.userMapper.deleteById(List.of(1L, 2L));
        this.userMapper.insert(new User(1L, "acgist"));
        this.userMapper.insert(new User(2L, "acgist"));
        final List<User> list = this.userMapper.selectAll();
        System.out.println(list);
    }
    
}
