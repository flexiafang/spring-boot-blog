package com.flexia.controller.admin;

import com.flexia.entity.Blog;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description 管理后台博客页的控制器
 * @Author hustffx
 * @Date 2020/7/14
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    /**
     * 跳转到博客列表页面
     *
     * @return
     */
    @GetMapping("/blogs")
    public String blogs(@RequestParam(required = false, defaultValue = "1") String page, Model model) {
        // 查询所有博客
        PageHelper.startPage(Integer.parseInt(page), 10);
        Page<Blog> blogs = (Page<Blog>) blogService.listBlog();
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        model.addAttribute("pageInfo", pageInfo);
        // 查询所有分类
        List<Type> types = typeService.listType();
        model.addAttribute("types", types);
        return "admin/blogs";
    }

    @PostMapping("/blogs/search")
    public String search(@RequestParam(required = false, defaultValue = "1") String page,
                         Model model, String title, Long typeId, Boolean recommend) {
        PageHelper.startPage(Integer.parseInt(page), 8);
        Page<Blog> blogPage = (Page<Blog>) blogService.getBlogByKeyWords("".equalsIgnoreCase(title) ? null : title, typeId, recommend);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogPage);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/blogs :: blogList";
    }

    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("blog", new Blog());
        return "admin/blog-input";
    }


}
