package com.flexia.controller;

import com.flexia.entity.Blog;
import com.flexia.entity.Tag;
import com.flexia.service.BlogService;
import com.flexia.service.TagService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description 前端标签页显示的控制器
 * @Author hustffx
 * @Date 2020/7/22
 */
@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{tagId}")
    public String types(@RequestParam(required = false, defaultValue = "1") String page,
                        @PathVariable Integer tagId, Model model) {
        List<Tag> tags = tagService.listTag(tagService.getTotal());
        model.addAttribute("tags", tags);
        PageHelper.startPage(Integer.parseInt(page), 5);
        if (tagId == -1) {
            tagId = tags.get(0).getTagId();
        }
        List<Blog> blogs = blogService.getBlogByTagId(tagId);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("activeTagId", tagId);
        return "tags";
    }
}
