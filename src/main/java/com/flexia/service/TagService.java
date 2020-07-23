package com.flexia.service;

import com.flexia.entity.Tag;

import java.util.List;

/**
 * @Description 标签的业务层接口
 * @Author hustffx
 * @Date 2020/7/14
 */
public interface TagService {

    Tag saveTag(Tag tag);

    Tag getTagById(Integer id);

    List<Tag> listTag();

    List<Tag> listTag(Integer size);

    List<Tag> listTag(String tagIds);

    Tag getTagByName(String name);

    Tag updateTag(Tag tag);

    int deleteTag(Integer id);

    Integer getTotal();
}
