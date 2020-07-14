package com.flexia.mapper;

import com.flexia.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Description 用户的持久层接口
 * @Author hustffx
 * @Date 2020/7/14
 */
public interface UserMapper extends Mapper<User> {

    @Select("select * from user where username = #{username} and password = #{password}")
    User findByUsernameAndPassWord(@Param("username") String username, @Param("password") String password);
}
