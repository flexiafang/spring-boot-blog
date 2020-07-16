package com.flexia.service;

import com.flexia.entity.Blog;
import com.flexia.entity.Type;

import java.util.List;

/**
 * @Description 博客的业务层接口
 * @Author hustffx
 * @Date 2020/7/15
 */
public interface BlogService {

    Blog getBlogById(Long blogId);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Blog blog);

    int deleteBlog(Long id);

    List<Blog> listBlog();

    List<Blog> getBlogByKeyWords(String title, Long typeId, Boolean recommend);
}
