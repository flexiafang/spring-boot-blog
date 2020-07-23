package com.flexia.service;

import com.flexia.entity.Blog;

import java.util.List;
import java.util.Map;

/**
 * @Description 博客的业务层接口
 * @Author hustffx
 * @Date 2020/7/15
 */
public interface BlogService {

    Blog getBlogById(Integer blogId);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Blog blog);

    int deleteBlog(Integer id);

    List<Blog> listBlog();

    List<Blog> listRecommendBlog(Integer size);

    List<Blog> listBlog(String query);

    List<Blog> getBlogByKeyWords(String title, Integer typeId, Boolean recommend);

    List<Blog> getBlogByTypeId(Integer typeId);

    List<Blog> getBlogByTagId(Integer tagId);

    Map<Integer, List<Blog>> archiveBlog();

    Blog getAndConvert(Integer blogId);

    Integer getTotal();
}
