package com.flexia.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description 管理后台博客页的控制器
 * @Author hustffx
 * @Date 2020/7/14
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    @GetMapping("/blogs")
    public String list() {
        return "admin/blogs";
    }
}
