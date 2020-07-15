package com.flexia.service;

import com.flexia.entity.Blog;
import com.github.pagehelper.Page;

/**
 * @Description 博客的业务层接口
 * @Author hustffx
 * @Date 2020/7/15
 */
public interface BlogService {

    Page<Blog> listBlog();

    Blog getBlogById(Long blogId);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Blog blog);

    int deleteBlog(Long id);
}
