package com.flexia.mapper;

import com.flexia.entity.Blog;
import com.flexia.entity.Type;
import com.flexia.entity.User;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Description 博客的持久层接口
 * @Author hustffx
 * @Date 2020/7/15
 */
public interface BlogMapper extends Mapper<Blog> {

    @Select("select * from blog order by update_time desc")
    @Results(id = "blogMap",
            value = {
                    @Result(column = "blog_id",property = "blogId"),
                    @Result(column = "title",property = "title"),
                    @Result(column = "content",property = "content"),
                    @Result(column = "first_picture", property = "firstPicture"),
                    @Result(column = "flag", property = "flag"),
                    @Result(column = "views", property = "views"),
                    @Result(column = "appreciation", property = "appreciation"),
                    @Result(column = "share_statement", property = "shareStatement"),
                    @Result(column = "comment", property = "comment"),
                    @Result(column = "publish", property = "publish"),
                    @Result(column = "recommend", property = "recommend"),
                    @Result(column = "update_time", property = "updateTime"),
                    @Result(column = "create_time", property = "createTime"),
                    @Result(column = "type_id", property = "type", javaType = Type.class,
                            one = @One(select = "com.flexia.mapper.TypeMapper.selectTypeById")),
                    @Result(column = "user_id", property = "user", javaType = User.class,
                            one = @One(select = "com.flexia.mapper.UserMapper.selectUserByUserId"))
            })
    List<Blog> findAllBlog();

    @Select("select * from blog where blog_id = #{id}")
    @ResultMap(value = "blogMap")
    Blog selectBlogByBlogId(@Param(value = "id") Integer id);

    @Select("select * from blog where type_id = #{typeId}")
    @ResultMap(value = "blogMap")
    List<Blog> selectBlogByTypeId(@Param(value = "typeId") Integer typeId);

    @Select("select date_format(blog.update_time, '%Y') as year from blog group by year order by year desc")
    List<Integer> findYearsGroupByYear();

    @Select("select * from blog where date_format(blog.update_time, '%Y') = #{year}")
    List<Blog> getBlogByYear(Integer year);
}
