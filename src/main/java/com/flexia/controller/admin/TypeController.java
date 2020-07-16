package com.flexia.controller.admin;

import com.flexia.entity.Type;
import com.flexia.service.TypeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @Description 分类的控制器
 * @Author hustffx
 * @Date 2020/7/14
 */
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    /**
     * 分页查询
     *
     * @param page  页码
     * @param model 视图模型
     * @return
     */
    @GetMapping("/types")
    public String types(@RequestParam(required = false, defaultValue = "1") String page, Model model) {
        // 设置分页参数
        PageHelper.startPage(Integer.parseInt(page), 10);
        // 查询所有分类
        Page<Type> types = (Page<Type>) typeService.listType();
        PageInfo<Type> pageInfo = new PageInfo<>(types);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/types";
    }

    /**
     * 跳转到新增分类页面
     *
     * @param model
     * @return
     */
    @GetMapping("/types/input")
    public String input(Model model) {
        model.addAttribute("type", new Type());
        return "admin/type-input";
    }

    /**
     * 新增分类
     *
     * @param type       分类
     * @param result     参数校验结果
     * @param attributes 重定向属性值
     * @return
     */
    @PostMapping("/types/input")
    public String inputPost(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
        Type t1 = typeService.getTypeByName(type.getName());

        if (t1 != null) {
            result.rejectValue("name", "nameError", "不能重复添加分类");
        }

        if (result.hasErrors()) {
            return "admin/type-input";
        }

        Type t2 = typeService.saveType(type);

        if (t2 == null) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }

        return "redirect:/admin/types";
    }

    /**
     * 跳转到编辑分类页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/types/{id}/update")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.getTypeById(id));
        return "admin/type-input";
    }

    /**
     * 更新分类名称
     *
     * @param type
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/types/update")
    public String editPost(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
        Type t1 = typeService.getTypeByName(type.getName());

        if (t1 != null) {
            result.rejectValue("name", "nameError", "不能更新为已有分类");
        }

        if (result.hasErrors()) {
            return "admin/type-input";
        }

        Type t2 = typeService.updateType(type);

        if (t2 == null) {
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }

        return "redirect:/admin/types";
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @GetMapping("/types/{id}/delete")
    public String deleteType(@PathVariable Long id, RedirectAttributes attributes) {
        int count = typeService.deleteType(id);

        if (count == 1) {
            attributes.addFlashAttribute("message", "删除成功");
        } else {
            attributes.addFlashAttribute("message", "删除失败，该分类不可删除");
        }

        return "redirect:/admin/types";
    }
}
