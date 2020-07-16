package com.flexia.mapper;

import com.flexia.entity.Blog;
import com.flexia.entity.Type;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author hustffx
 * @Date 2020/7/15
 */
public interface BlogMapper extends Mapper<Blog> {
}
