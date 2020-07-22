package com.flexia.controller;

import com.flexia.entity.Comment;
import com.flexia.entity.User;
import com.flexia.service.BlogService;
import com.flexia.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/**
 * @Description 评论的控制器
 * @Author hustffx
 * @Date 2020/7/22
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Integer blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            comment.setAvatar(avatar);
        } else {
            comment.setAvatar(user.getAvatar());
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + comment.getBlogId();
    }
}
