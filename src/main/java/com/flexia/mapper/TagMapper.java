package com.flexia.mapper;

import com.flexia.entity.Tag;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Description 分类的持久层接口
 * @Author hustffx
 * @Date 2020/7/14
 */
public interface TagMapper extends Mapper<Tag> {

    @Select("select distinct tag.*, count(blog_tag.blog_tag_id) as blogNum from tag left join blog_tag " +
            "on tag.tag_id = blog_tag.tag_id group by tag.tag_id order by blogNum desc limit #{size}")
    List<Tag> getTopTags(@Param("size") Integer size);
}
