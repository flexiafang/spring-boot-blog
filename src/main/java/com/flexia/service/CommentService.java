package com.flexia.service;

import com.flexia.entity.Comment;

import java.util.List;

/**
 * @Description 评论的业务层接口
 * @Author hustffx
 * @Date 2020/7/22
 */
public interface CommentService {

    List<Comment> listCommentByBlogId(Integer blogId);

    void saveComment(Comment comment);
}
