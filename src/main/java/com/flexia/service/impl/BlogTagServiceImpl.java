package com.flexia.service.impl;

import com.flexia.entity.Blog;
import com.flexia.entity.BlogTag;
import com.flexia.entity.Tag;
import com.flexia.mapper.BlogTagMapper;
import com.flexia.service.BlogService;
import com.flexia.service.BlogTagService;
import com.flexia.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author hustffx
 * @Date 2020/7/21
 */
@Service
public class BlogTagServiceImpl implements BlogTagService {

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    /**
     * 根据博客id查询博客与标签的对应关系集合
     *
     * @param blogId
     * @return
     */
    @Override
    public List<BlogTag> queryBlogTag(Integer blogId) {
        Example example = new Example(BlogTag.class);
        example.createCriteria().andEqualTo("blogId", blogId);
        return blogTagMapper.selectByExample(example);
    }

    /**
     * 根据博客id获取标签集
     *
     * @param blogId
     * @return
     */
    @Override
    public List<Tag> getTagsByBlogId(Integer blogId) {
        List<BlogTag> blogTags = this.queryBlogTag(blogId);
        List<Tag> tags = new ArrayList<>();
        for (BlogTag blogTag : blogTags) {
            tags.add(tagService.getTagById(blogTag.getTagId()));
        }
        return tags;
    }

    /**
     * 根据博客id获取标签集合对应的字符串
     *
     * @param blogId
     * @return
     */
    @Override
    public String getTagIdsByBlogId(Integer blogId) {
        List<BlogTag> blogTags = this.queryBlogTag(blogId);
        StringBuilder tagIds = new StringBuilder();
        boolean sign = false;
        for (BlogTag blogTag : blogTags) {
            if (sign) {
                tagIds.append(",");
            } else {
                sign = true;
            }
            tagIds.append(blogTag.getTagId());
        }
        return tagIds.toString();
    }

    @Override
    public List<Blog> getBlogsByTagId(Integer tagId) {
        Example example = new Example(BlogTag.class);
        example.createCriteria().andEqualTo("tagId", tagId);
        List<BlogTag> blogTags = blogTagMapper.selectByExample(example);
        List<Blog> blogs = new ArrayList<>();
        for (BlogTag blogTag : blogTags) {
            blogs.add(blogService.getBlogById(blogTag.getBlogId()));
        }
        return blogs;
    }

    /**
     * 保存博客标签对应关系
     *
     * @param blog
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveBlogTag(Blog blog) {
        BlogTag blogTag = new BlogTag();
        blogTag.setBlogId(blog.getBlogId());
        for (Tag tag : blog.getTags()) {
            blogTag.setBlogTagId(null);
            blogTag.setTagId(tag.getTagId());
            blogTagMapper.insert(blogTag);
        }
    }

    /**
     * 更新博客标签对应关系
     *
     * @param blog
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBlogTag(Blog blog) {
        Integer blogId = blog.getBlogId();
        List<Tag> newTags = blog.getTags();
        List<Tag> oldTags = this.getTagsByBlogId(blogId);

        BlogTag blogTag = new BlogTag();
        blogTag.setBlogId(blogId);

        // 删除已经不存在的对应关系
        for (Tag oldTag : oldTags) {
            if (!newTags.contains(oldTag)) {
                blogTag.setTagId(oldTag.getTagId());
                blogTagMapper.delete(blogTag);
            }
        }

        // 添加新的对应关系
        for (Tag newTag : newTags) {
            if (!oldTags.contains(newTag)) {
                blogTag.setBlogTagId(null);
                blogTag.setTagId(newTag.getTagId());
                blogTagMapper.insert(blogTag);
            }
        }
    }

    /**
     * 删除所有的博客标签对应关系
     *
     * @param blogId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBlogTag(Integer blogId) {
        Example example = new Example(BlogTag.class);
        example.createCriteria().andEqualTo("blogId", blogId);
        blogTagMapper.deleteByExample(example);
    }
}
