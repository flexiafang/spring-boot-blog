package com.flexia.service.impl;

import com.flexia.entity.User;
import com.flexia.mapper.UserMapper;
import com.flexia.service.UserService;
import com.flexia.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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
        Example example = new Example(User.class);
        example.createCriteria()
                .andEqualTo("username", username)
                .andEqualTo("password", password);
        return userMapper.selectOneByExample(example);
    }

    @Override
    public User getUserById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }
}
