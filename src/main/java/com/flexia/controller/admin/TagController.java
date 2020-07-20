package com.flexia.controller.admin;

import com.flexia.entity.Tag;
import com.flexia.service.TagService;
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
 * @Description 标签的控制器
 * @Author hustffx
 * @Date 2020/7/14
 */
@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 分页查询
     *
     * @param page  页码
     * @param model 视图模型
     * @return
     */
    @GetMapping("/tags")
    public String tags(@RequestParam(required = false, defaultValue = "1") String page, Model model) {
        // 设置分页参数
        PageHelper.startPage(Integer.parseInt(page), 10);
        // 查询所有标签
        Page<Tag> tags = (Page<Tag>) tagService.listTag();
        PageInfo<Tag> pageInfo = new PageInfo<>(tags);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/tags";
    }

    /**
     * 跳转到新增标签页面
     *
     * @param model
     * @return
     */
    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tag-input";
    }

    /**
     * 新增标签
     *
     * @param tag       标签
     * @param result     参数校验结果
     * @param attributes 重定向属性值
     * @return
     */
    @PostMapping("/tags/input")
    public String inputPost(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {
        Tag t1 = tagService.getTagByName(tag.getName());

        if (t1 != null) {
            result.rejectValue("name", "nameError", "不能重复添加标签");
        }

        if (result.hasErrors()) {
            return "admin/tag-input";
        }

        Tag t2 = tagService.saveTag(tag);

        if (t2 == null) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }

        return "redirect:/admin/tags";
    }

    /**
     * 跳转到编辑标签页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/tags/{id}/update")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("tag", tagService.getTagById(id));
        return "admin/tag-input";
    }

    /**
     * 更新标签名称
     *
     * @param tag
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/tags/update")
    public String editPost(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {
        Tag t1 = tagService.getTagByName(tag.getName());

        if (t1 != null) {
            result.rejectValue("name", "nameError", "不能更新为已有标签");
        }

        if (result.hasErrors()) {
            return "admin/tag-input";
        }

        Tag t2 = tagService.updateTag(tag);

        if (t2 == null) {
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }

        return "redirect:/admin/tags";
    }

    /**
     * 删除标签
     *
     * @param id
     * @return
     */
    @GetMapping("/tags/{id}/delete")
    public String deleteTag(@PathVariable Integer id, RedirectAttributes attributes) {
        int count = tagService.deleteTag(id);

        if (count == 1) {
            attributes.addFlashAttribute("message", "删除成功");
        } else {
            attributes.addFlashAttribute("message", "删除失败，该标签不可删除");
        }

        return "redirect:/admin/tags";
    }
}
