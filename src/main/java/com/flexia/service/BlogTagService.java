package com.flexia.service;

import com.flexia.entity.Blog;
import com.flexia.entity.BlogTag;
import com.flexia.entity.Tag;

import java.util.List;

/**
 * @Description 博客标签对应关系的业务层接口
 * @Author hustffx
 * @Date 2020/7/21
 */
public interface BlogTagService {

    List<BlogTag> queryBlogTag(Integer blogId);

    List<Tag> getTagsByBlogId(Integer blogId);

    String getTagIdsByBlogId(Integer blogId);

    List<Blog> getBlogsByTagId(Integer tagId);

    void saveBlogTag(Blog blog);

    void updateBlogTag(Blog blog);

    void deleteBlogTag(Integer blogId);
}
