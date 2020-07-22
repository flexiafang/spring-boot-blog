package com.flexia.mapper;

import com.flexia.entity.Type;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Description 分类的持久层接口
 * @Author hustffx
 * @Date 2020/7/14
 */
public interface TypeMapper extends Mapper<Type> {

    @Select("select distinct type.*, count(blog.blog_id) as blogNum from type left join blog " +
            "on type.type_id = blog.type_id group by type.type_id order by blogNum desc limit #{size}")
    List<Type> getTopTypes(@Param("size") Integer size);
}
