package com.flexia.controller.admin;

import com.flexia.entity.Blog;
import com.flexia.entity.Type;
import com.flexia.entity.User;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
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

    /**
     * 根据前端输入来搜索博客
     *
     * @param page
     * @param model
     * @param title
     * @param typeId
     * @param recommend
     * @return
     */
    @PostMapping("/blogs/search")
    public String search(@RequestParam(required = false, defaultValue = "1") String page,
                         Model model, String title, Long typeId, Boolean recommend) {
        PageHelper.startPage(Integer.parseInt(page), 8);
        Page<Blog> blogPage = (Page<Blog>) blogService.getBlogByKeyWords("".equalsIgnoreCase(title) ? null : title, typeId, recommend);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogPage);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/blogs :: blogList";
    }

    /**
     * 跳转到新增博客页面
     *
     * @param model
     * @return
     */
    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("blog", new Blog());
        return "admin/blog-input";
    }


    @PostMapping("/blogs/post")
    public String post(Blog blog, String tagIds, HttpSession session, RedirectAttributes attributes) {
        // 设置博客的相关信息
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getTypeById(blog.getTypeId()));
        blog.setTags(tagService.listTag(tagIds));

        Blog b = blogService.saveBlog(blog);

        if (b == null) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/blogs";
    }
}
