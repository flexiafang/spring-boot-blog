package com.flexia.service;

import com.flexia.entity.Tag;
import com.github.pagehelper.Page;

/**
 * @Description 分类的业务层接口
 * @Author hustffx
 * @Date 2020/7/14
 */
public interface TagService {

    Tag saveTag(Tag tag);

    Tag getTagById(Long id);

    Page<Tag> listTag();

    Tag getTagByName(String name);

    Tag updateTag(Tag tag);

    int deleteTag(Long id);
}
