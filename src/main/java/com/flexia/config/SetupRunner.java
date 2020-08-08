package com.flexia.config;

import com.flexia.entity.Blog;
import com.flexia.mapper.BlogMapper;
import com.flexia.util.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author hustffx
 * @Date 2020/8/8
 */
@Component
@Slf4j
public class SetupRunner implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public void run(String... args) throws Exception {
        List<Blog> list = blogMapper.findAllBlog();
        for (Blog blog : list) {
            redisTemplate.opsForHash().put(RedisKeyUtils.ALL_BLOG, blog.getBlogId(), blog);
            log.info("将blog数据添加到redis的AllBlog中，其博客标题为：" + blog.getTitle());
        }
        redisTemplate.opsForValue().set(RedisKeyUtils.BLOG_COUNT, list.size());
        log.info("初始化redis中blogCount的数量为：" + list.size());
    }
}
