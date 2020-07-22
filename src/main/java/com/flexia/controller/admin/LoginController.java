package com.flexia.controller.admin;

import com.flexia.entity.User;
import com.flexia.service.UserService;
import com.flexia.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @Description 登录后台的控制器
 * @Author hustffx
 * @Date 2020/7/14
 */
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 跳转到登录页面
     *
     * @return
     */
    @GetMapping
    public String loginPage() {
        return "admin/login";
    }

    /**
     * 提交登录信息，进行用户校验
     *
     * @param username
     * @param password
     * @param session
     * @param attributes
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes) {
        User user = userService.checkUser(username, MD5Utils.code(password));
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user);
            return "redirect:/admin/index";
        } else {
            attributes.addFlashAttribute("message", "用户名或密码错误");
            return "redirect:/admin";
        }
    }

    /**
     * 登录成功跳转至 index 页面
     *
     * @return
     */
    @GetMapping("/index")
    public String index() {
        return "admin/index";
    }

    /**
     * 注销登录
     *
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
