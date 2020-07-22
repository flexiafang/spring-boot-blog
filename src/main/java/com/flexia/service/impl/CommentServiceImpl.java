package com.flexia.service.impl;

import com.flexia.entity.Comment;
import com.flexia.mapper.CommentMapper;
import com.flexia.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author hustffx
 * @Date 2020/7/22
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveComment(Comment comment) {
        comment.setCreateTime(new Date());
        commentMapper.insert(comment);
    }

    @Override
    public List<Comment> listCommentByBlogId(Integer blogId) {
        Example example = new Example(Comment.class);
        example.createCriteria()
                .andEqualTo("blogId", blogId)
                .andEqualTo("parentCommentId", -1);
        example.orderBy("createTime");
        List<Comment> comments = commentMapper.selectByExample(example);
        return eachComment(comments);
    }

    /**
     * 循环每个顶级的评论节点
     *
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>(comments);
        // 合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replys = this.getReplyComments(comment);
            recursively(replys);
            // 修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            // 清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    // 存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();

    /**
     * 递归迭代，剥洋葱
     *
     * @param comments 被迭代的对象
     * @return
     */
    private void recursively(List<Comment> comments) {
        // 顶节点添加到临时存放集合
        tempReplys.addAll(comments);
        for (Comment comment : comments) {
            List<Comment> replys = this.getReplyComments(comment);
            if (replys.size() > 0) {
                recursively(replys);
            }
        }
    }

    private List<Comment> getReplyComments(Comment comment) {
        Example example = new Example(Comment.class);
        example.createCriteria()
                .andEqualTo("blogId", comment.getBlogId())
                .andEqualTo("parentCommentId", comment.getCommentId());
        example.setOrderByClause("create_time asc");
        List<Comment> replyComments = commentMapper.selectByExample(example);
        this.setParentComment(comment, replyComments);
        return replyComments;
    }

    private void setParentComment(Comment comment, List<Comment> replyComments) {
        for (Comment reply : replyComments) {
            reply.setParentComment(comment);
        }
    }
}
