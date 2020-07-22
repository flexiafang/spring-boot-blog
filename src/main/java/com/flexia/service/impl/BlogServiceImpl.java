package com.flexia.service.impl;

import com.flexia.entity.Blog;
import com.flexia.exception.NotFoundException;
import com.flexia.mapper.BlogMapper;
import com.flexia.service.BlogService;
import com.flexia.service.BlogTagService;
import com.flexia.service.TypeService;
import com.flexia.service.UserService;
import com.flexia.util.MarkdownUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.Date;
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

    @Autowired
    private BlogTagService blogTagService;

    @Autowired
    private UserService userService;

    @Override
    public Blog getBlogById(Integer blogId) {
        Blog blog = blogMapper.selectByPrimaryKey(blogId);
        this.setBlogProperties(Collections.singletonList(blog));
        return blog;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Blog saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);
        int count = blogMapper.insert(blog);
        if (count == 0) {
            return null;
        } else {
            blogTagService.saveBlogTag(blog);
            return blog;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Blog updateBlog(Blog blog) {
        Blog b = blogMapper.selectByPrimaryKey(blog);
        if (b == null) {
            throw new NotFoundException("不存在该博客");
        }
        BeanUtils.copyProperties(blog, b);
        b.setUpdateTime(new Date());
        blogMapper.updateByPrimaryKey(b);
        blogTagService.updateBlogTag(b);
        return b;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteBlog(Integer id) {
        int count = 0;
        try {
            blogTagService.deleteBlogTag(id);
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
        this.setBlogProperties(blogs);
        return blogs;
    }

    /**
     * 获取最新推荐的博客列表
     *
     * @return
     */
    @Override
    public List<Blog> listRecommendBlog(Integer size) {
        Example example = new Example(Blog.class);
        example.setOrderByClause("update_time desc limit " + size);
        List<Blog> blogs = blogMapper.selectByExample(example);
        this.setBlogProperties(blogs);
        return blogs;
    }

    /**
     * 根据搜索的关键词查询博客列表
     *
     * @param query
     * @return
     */
    @Override
    public List<Blog> listBlog(String query) {
        query = "%" + query + "%";
        Example example = new Example(Blog.class);
        example.createCriteria()
                .orLike("title", query)
                .orLike("content", query);
        List<Blog> blogs = blogMapper.selectByExample(example);
        this.setBlogProperties(blogs);
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
    public List<Blog> getBlogByKeyWords(String title, Integer typeId, Boolean recommend) {
        // 设置查询条件
        Example example = new Example(Blog.class);
        Example.Criteria criteria = example.createCriteria();

        if (title != null) {
            criteria.andLike("title", "%" + title + "%");
        }
        if (typeId != null) {
            criteria.andEqualTo("typeId", typeId);
        }
        if (recommend) {
            criteria.andEqualTo("recommend", true);
        }

        List<Blog> blogs = blogMapper.selectByExample(example);
        setBlogProperties(blogs);
        return blogs;
    }

    /**
     * 根据分类id查询博客列表
     *
     * @param typeId
     * @return
     */
    @Override
    public List<Blog> getBlogByTypeId(Integer typeId) {
        Example example = new Example(Blog.class);
        example.createCriteria().andEqualTo("typeId", typeId);
        List<Blog> blogs = blogMapper.selectByExample(example);
        this.setBlogProperties(blogs);
        return blogs;
    }

    /**
     * 获取博客并转换为html文本
     *
     * @param blogId
     * @return
     */
    @Override
    public Blog getAndConvert(Integer blogId) {
        Blog blog = blogMapper.selectByPrimaryKey(blogId);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        content = MarkdownUtils.markdownToHtmlExtensions(content);
        b.setContent(content);
        this.setBlogProperties(Collections.singletonList(b));
        return b;
    }

    /**
     * 设置查询得到的博客的相关信息
     *
     * @param blogs
     */
    private void setBlogProperties(List<Blog> blogs) {
        for (Blog blog : blogs) {
            // 分类
            blog.setType(typeService.getTypeById(blog.getTypeId()));
            // 标签
            blog.setTags(blogTagService.getTagsByBlogId(blog.getBlogId()));
            // 用户
            blog.setUser(userService.getUserById(blog.getUserId()));
        }
    }
}
