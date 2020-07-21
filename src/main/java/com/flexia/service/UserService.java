package com.flexia.service;

import com.flexia.entity.User;

/**
 * @Description 用户的业务层接口
 * @Author hustffx
 * @Date 2020/7/8 0:59
 */
public interface UserService {
    /**
     * 检查登录用户，核对用户名和密码
     *
     * @param username
     * @param password
     * @return
     */
    User checkUser(String username, String password);

    User getUserById(Integer userId);
}
