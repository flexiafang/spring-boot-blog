package com.flexia.controller;

import com.flexia.entity.Blog;
import com.flexia.entity.Type;
import com.flexia.service.BlogService;
import com.flexia.service.TypeService;
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
 * @Author hustffx
 * @Date 2020/7/22
 */
@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{typeId}")
    public String types(@RequestParam(required = false, defaultValue = "1") String page,
                        @PathVariable Integer typeId, Model model) {
        List<Type> types = typeService.listType(typeService.getTotal());
        model.addAttribute("types", types);
        PageHelper.startPage(Integer.parseInt(page), 5);
        if (typeId == -1) {
            typeId = types.get(0).getTypeId();
        }
        List<Blog> blogs = blogService.getBlogByTypeId(typeId);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("activeTypeId", typeId);
        return "types";
    }
}
