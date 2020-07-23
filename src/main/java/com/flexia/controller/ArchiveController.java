package com.flexia.controller;

import com.flexia.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author hustffx
 * @Date 2020/7/23
 */
@Controller
public class ArchiveController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model) {
        model.addAttribute("archiveMap", blogService.archiveBlog());
        model.addAttribute("blogCount", blogService.getTotal());
        return "archives";
    }
}
