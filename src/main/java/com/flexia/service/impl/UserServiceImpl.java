package com.flexia.service.impl;

import com.flexia.entity.User;
import com.flexia.mapper.UserMapper;
import com.flexia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author hustffx
 * @Date 2020/7/14
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User checkUser(String username, String password) {
        return userMapper.findByUsernameAndPassWord(username, password);
    }
}
