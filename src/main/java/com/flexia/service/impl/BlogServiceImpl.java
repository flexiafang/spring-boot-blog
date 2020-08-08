package com.flexia.service.impl;

import com.flexia.entity.Blog;
import com.flexia.entity.Type;
import com.flexia.exception.NotFoundException;
import com.flexia.mapper.BlogMapper;
import com.flexia.service.BlogService;
import com.flexia.service.BlogTagService;
import com.flexia.service.TypeService;
import com.flexia.service.UserService;
import com.flexia.util.MarkdownUtils;
import com.flexia.util.RedisKeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @Author hustffx
 * @Date 2020/7/15
 */
@Service
public class BlogServiceImpl implements BlogService {

    private static final Logger logger = LoggerFactory.getLogger(BlogService.class);

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogTagService blogTagService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Blog getBlogById(Integer blogId) {
        // 尝试从redis中获取
        Blog blog = (Blog) redisTemplate.opsForHash().get(RedisKeyUtils.ALL_BLOG, blogId);
        if (blog != null) {
            logger.info("从redis中获取blog的数据，标题为" + blog.getTitle());
        } else {
            blog = blogMapper.selectByPrimaryKey(blogId);
            this.setBlogProperties(Collections.singletonList(blog));
            // 存入redis
            redisTemplate.opsForHash().put(RedisKeyUtils.ALL_BLOG, blog.getBlogId(), blog);
            logger.info("将blog信息存入redis中，标题为" + blog.getTitle());
        }
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
            // 更新redis数据库中的BlogCount属性值
            redisTemplate.opsForValue().increment(RedisKeyUtils.BLOG_COUNT);
            logger.info("更新redis中BlogCount的数值,变化为:" + "+1");
            // 更新redis中BlogTypes中相关的数据
            Type type = (Type) redisTemplate.opsForHash().get(RedisKeyUtils.BLOG_TYPES, blog.getTypeId());
            logger.info("更新redis中BlogTypes中相关的数据:" + type);
            if (type != null) {
                type.setBlogNum(type.getBlogNum() + 1);
                redisTemplate.opsForHash().put(RedisKeyUtils.BLOG_TYPES, type.getTypeId(), type);
                logger.info("更新redis中BlogTypes中相关的数据:" + type);
            }

            blogTagService.saveBlogTag(blog);

            // 更新redis中BlogTags中相关的数据(由于一个博客可能对应无数个标签，故在此将redis中的BlogTags的key删除)
            redisTemplate.delete(RedisKeyUtils.BLOG_TAGS);
            logger.info("删除redis中key为blogtags的hash数据类型");
            Blog blog1 = blogMapper.selectByPrimaryKey(blog.getBlogId());
            blog.setCreateTime(blog1.getCreateTime());
            blog.setViews(blog1.getViews());
            // 更新TheLatestBlog数据
            redisTemplate.opsForHash().put(RedisKeyUtils.THE_LATEST_BLOG, blog.getBlogId(), blog);
            logger.info("更新redis中TheLatestBlog的数据，标题为" + blog.getTitle());
            // 更新AllBlog数据
            redisTemplate.opsForHash().put(RedisKeyUtils.ALL_BLOG, blog.getBlogId(), blog);
            logger.info("更新redis中AllBlog的数据，标题为" + blog.getTitle());

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
        Type type1 = typeService.getTypeById(b.getTypeId());

        BeanUtils.copyProperties(blog, b);
        b.setUpdateTime(new Date());
        blogMapper.updateByPrimaryKey(b);

        // 更新redis中BlogTypes中相关的数据
        if (!blog.getTypeId().equals(type1.getTypeId())) {
            type1.setBlogNum(type1.getBlogNum() - 1);
            redisTemplate.opsForHash().put(RedisKeyUtils.BLOG_TYPES, type1.getTypeId(), type1);
            logger.info("更新redis中BlogTypes中相关的数据:" + type1);

            Type type = (Type) redisTemplate.opsForHash().get(RedisKeyUtils.BLOG_TYPES, blog.getTypeId());
            logger.info("更新redis中BlogTypes中相关的数据:" + type);
            if (type != null) {
                type.setBlogNum(type.getBlogNum() + 1);
                redisTemplate.opsForHash().put(RedisKeyUtils.BLOG_TYPES, type.getTypeId(), type);
                logger.info("更新redis中BlogTypes中相关的数据:" + type);
            }
        }

        blogTagService.updateBlogTag(b);

        // 更新redis中BlogTags中相关的数据(由于一个博客可能对应无数个标签，故在此将redis中的BlogTags的key删除)
        redisTemplate.delete(RedisKeyUtils.BLOG_TAGS);
        logger.info("删除redis中key为blogtags的hash数据类型");
        Blog blog1 = blogMapper.selectByPrimaryKey(blog.getBlogId());
        b.setCreateTime(blog1.getCreateTime());
        b.setViews(blog1.getViews());
        // 更新TheLatestBlog数据
        redisTemplate.opsForHash().put(RedisKeyUtils.THE_LATEST_BLOG, blog.getBlogId(), b);
        logger.info("更新redis中TheLatestBlog的数据，标题为" + b.getTitle());
        // 更新AllBlog数据
        redisTemplate.opsForHash().put(RedisKeyUtils.ALL_BLOG, blog.getBlogId(), b);
        logger.info("更新redis中AllBlog的数据，标题为" + b.getTitle());

        return b;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteBlog(Integer id) {
        Blog blog = blogMapper.selectBlogByBlogId(id);
        int count = 0;
        try {
            blogTagService.deleteBlogTag(id);
            count = blogMapper.deleteByPrimaryKey(id);

            if (count != 0) {
                //更新redis数据库中的BlogCount属性值
                redisTemplate.opsForValue().decrement(RedisKeyUtils.BLOG_COUNT);
                logger.info("更新redis中BlogCount的数值,变化为：" + "-1");
                //更新redis中BlogTypes中相关的数据
                Type type = (Type) redisTemplate.opsForHash().get(RedisKeyUtils.BLOG_TYPES, blog.getTypeId());
                logger.info("从redis中获取到typeTops的数据：" + type);
                if (type != null) {
                    type.setBlogNum(type.getBlogNum() - 1);
                    redisTemplate.opsForHash().put(RedisKeyUtils.BLOG_TYPES, type.getTypeId(), type);
                    logger.info("更新redis中BlogTypes中相关的数据" + type);
                }
                //更新redis中BlogTags中相关的数据(由于一个博客可能对应无数个标签，故在此将redis中的BlogTags的key删除)
                redisTemplate.delete(RedisKeyUtils.BLOG_TAGS);
                logger.info("删除redis中key为blogtags的hash数据类型");
                //删除redis中TheLatestBlog中的数据
                redisTemplate.opsForHash().delete(RedisKeyUtils.THE_LATEST_BLOG, blog.getBlogId());
                logger.info("删除redis中TheLatestBlog中有关该博客的数据，标题为" + blog.getTitle());
                redisTemplate.opsForHash().delete(RedisKeyUtils.ALL_BLOG, blog.getBlogId());
                logger.info("删除redis中AllBlog中有关该博客的数据，标题为" + blog.getTitle());
            }

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
        // 先尝试从redis中获取
        List<Object> values = redisTemplate.opsForHash().values(RedisKeyUtils.THE_LATEST_BLOG);
        logger.info("从redis中获取最新博客");
        if (values.size() > 0) {
            List<Blog> blogs = new ArrayList<>();
            for (Object object : values) {
                blogs.add((Blog) object);
            }
            blogs.sort((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime()));
            return blogs;
        }

        // redis中不存在再从mysql数据库中获取
        Example example = new Example(Blog.class);
        example.setOrderByClause("update_time desc limit " + size);
        List<Blog> blogs = blogMapper.selectByExample(example);
        this.setBlogProperties(blogs);

        for (Blog blog : blogs) {
            //存入redis
            redisTemplate.opsForHash().put(RedisKeyUtils.THE_LATEST_BLOG, blog.getBlogId(), blog);
            logger.info("将blog数据添加到TheLatestBlog中，标题为" + blog.getTitle());
        }

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
     * 根据标签id查询博客列表
     *
     * @param tagId
     * @return
     */
    @Override
    public List<Blog> getBlogByTagId(Integer tagId) {
        List<Blog> blogs = blogTagService.getBlogsByTagId(tagId);
        this.setBlogProperties(blogs);
        return blogs;
    }

    /**
     * 博客归档
     *
     * @return
     */
    @Override
    public Map<Integer, List<Blog>> archiveBlog() {
        List<Integer> years = blogMapper.findYearsGroupByYear();
        Map<Integer, List<Blog>> map = new LinkedHashMap<>(4);
        for (Integer year : years) {
            List<Blog> blogs = blogMapper.getBlogByYear(year);
            List<Blog> blogList = new ArrayList<>();
            for (Blog blog : blogs) {
                blogList.add(blogMapper.selectOne(blog));
            }
            this.setBlogProperties(blogList);
            map.put(year, blogList);
        }
        return map;
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

    @Override
    public Integer getTotal() {
        // 先从redis中取得数据，若不存在再从mysql中获取数据
        Integer count = (Integer) redisTemplate.opsForValue().get(RedisKeyUtils.BLOG_COUNT);
        logger.info("从redis中获取到博客总数量为：" + count);
        if (count != null) {
            return count;
        }
        return blogMapper.selectCount(null);
    }
}
