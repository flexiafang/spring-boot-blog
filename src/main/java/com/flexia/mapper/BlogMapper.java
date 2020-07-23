package com.flexia.mapper;

import com.flexia.entity.Blog;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Description 博客的持久层接口
 * @Author hustffx
 * @Date 2020/7/15
 */
public interface BlogMapper extends Mapper<Blog> {

    @Select("select date_format(blog.update_time, '%Y') as year from blog group by year order by year desc")
    List<Integer> findYearsGroupByYear();

    @Select("select * from blog where date_format(blog.update_time, '%Y') = #{year}")
    List<Blog> getBlogByYear(Integer year);
}
