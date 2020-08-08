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
    User selectUserNameByNameAndPassWord(String username,String password);


    @Select("select * from user where user_id = #{id}")
    User selectUserByUserId(@Param(value = "id") String id);
}
