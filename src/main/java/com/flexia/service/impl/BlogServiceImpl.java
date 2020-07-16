package com.flexia.service.impl;

import com.flexia.entity.Blog;
import com.flexia.entity.Type;
import com.flexia.exception.NotFoundException;
import com.flexia.mapper.BlogMapper;
import com.flexia.service.BlogService;
import com.flexia.service.TypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author hustffx
 * @Date 2020/7/15
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private TypeService typeService;

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

    /**
     * 查询所有博客
     *
     * @return
     */
    @Override
    public List<Blog> listBlog() {
        List<Blog> blogs = blogMapper.selectAll();
        setBlogProperties(blogs);
        return blogs;
    }

    /**
     * 根据查询条件查询博客列表
     *
     * @param title
     * @param typeId
     * @param recommend
     * @return
     */
    @Override
    public List<Blog> getBlogByKeyWords(String title, Long typeId, Boolean recommend) {
        // 设置查询条件
        Example example = new Example(Blog.class);
        Example.Criteria criteria = example.createCriteria();

        if (!"".equals(title) && title != null) {
            criteria.andLike("title", title);
        }
        if (typeId != null) {
            criteria.andEqualTo("type_id", typeId);
        }
        if (recommend) {
            criteria.andEqualTo("recommend", true);
        }

        List<Blog> blogs = blogMapper.selectByExample(example);
        setBlogProperties(blogs);
        return blogs;
    }

    /**
     * 设置查询得到的博客的相关信息
     *
     * @param blogs
     */
    private void setBlogProperties(List<Blog> blogs) {
        for (Blog blog : blogs) {
            // 获取分类
            Type type = typeService.getTypeById(blog.getTypeId());
            blog.setType(type);
        }
    }
}
