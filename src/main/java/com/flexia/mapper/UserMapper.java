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
}
