package com.flexia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author hustffx
 * @Date 2020/7/22
 */
@Controller
public class TypeShowController {

    @GetMapping("/types/{id}")
    public String types() {
        return "types";
    }
}
