package com.flexia.mapper;

import com.flexia.entity.Comment;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Description 评论的持久层接口
 * @Author hustffx
 * @Date 2020/7/22
 */
public interface CommentMapper extends Mapper<Comment> {
}
