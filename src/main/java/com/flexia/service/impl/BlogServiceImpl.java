package com.flexia.service.impl;

import com.flexia.entity.Blog;
import com.flexia.exception.NotFoundException;
import com.flexia.mapper.BlogMapper;
import com.flexia.service.BlogService;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author hustffx
 * @Date 2020/7/15
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public Page<Blog> listBlog() {
        return (Page<Blog>) blogMapper.selectAll();
    }

    @Override
    public Blog getBlogById(Long blogId) {
        return blogMapper.selectByPrimaryKey(blogId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Blog saveBlog(Blog blog) {
        int count = blogMapper.insert(blog);
        return count == 0 ? null : blog;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Blog updateBlog(Blog blog) {
        Blog b = blogMapper.selectByPrimaryKey(blog);
        if (b == null) {
            throw new NotFoundException("不存在该博客");
        }
        BeanUtils.copyProperties(blog, b);
        blogMapper.updateByPrimaryKey(b);
        return b;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteBlog(Long id) {
        int count = 0;
        try {
            count = blogMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Cause exception when delete blog. {}", e.getMessage());
        }
        return count;
    }
}
