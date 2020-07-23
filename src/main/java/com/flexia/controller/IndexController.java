package com.flexia.controller;

import com.flexia.entity.Blog;
import com.flexia.entity.Tag;
import com.flexia.entity.Type;
import com.flexia.service.BlogService;
import com.flexia.service.TagService;
import com.flexia.service.TypeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description 首页的控制器
 * @Author hustffx
 * @Date 2020/7/5 23:48
 */
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@RequestParam(required = false, defaultValue = "1") String page, Model model) {
        PageHelper.startPage(Integer.parseInt(page), 5);
        Page<Blog> blogPage = (Page<Blog>) blogService.listBlog();
        PageInfo<Blog> pageInfo = new PageInfo<>(blogPage);
        model.addAttribute("pageInfo", pageInfo);

        List<Type> types = typeService.listType(10);
        model.addAttribute("types", types);

        List<Tag> tags = tagService.listTag(10);
        model.addAttribute("tags", tags);

        List<Blog> blogs = blogService.listRecommendBlog(10);
        model.addAttribute("recommendBlogs", blogs);
        return "index";
    }

    @PostMapping("/search")
    public String search(@RequestParam(required = false, defaultValue = "1") String page,
                         @RequestParam String query, Model model) {
        PageHelper.startPage(Integer.parseInt(page), 5);
        Page<Blog> blogs = (Page<Blog>) blogService.listBlog(query);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Integer id, Model model) {
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/footer/newBlogList")
    public String newBlogs(Model model) {
        model.addAttribute("newBlogs", blogService.listRecommendBlog(3));
        return "_fragments :: newBlogList";
    }
}
