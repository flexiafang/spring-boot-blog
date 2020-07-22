package com.flexia.service.impl;

import com.flexia.entity.Tag;
import com.flexia.exception.NotFoundException;
import com.flexia.mapper.TagMapper;
import com.flexia.service.BlogTagService;
import com.flexia.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author hustffx
 * @Date 2020/7/14
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private BlogTagService blogTagService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Tag saveTag(Tag tag) {
        int count = tagMapper.insert(tag);
        return count == 1 ? tag : null;
    }

    @Override
    public Tag getTagById(Integer id) {
        return tagMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Tag> listTag() {
        return tagMapper.selectAll();
    }

    /**
     * 获取使用量在前size位的标签集
     *
     * @param size
     * @return
     */
    @Override
    public List<Tag> listTag(Integer size) {
        return tagMapper.getTopTags(size);
    }

    @Override
    public List<Tag> listTag(String tagIds) {
        List<Tag> tags = new ArrayList<>();
        List<Integer> ids = convertTagIdsToList(tagIds);
        for (Integer id : ids) {
            Tag tag = tagMapper.selectByPrimaryKey(id);
            tags.add(tag);
        }
        return tags;
    }

    /**
     * 将字符串形式的tagIds转换为列表
     *
     * @param tagIds
     * @return
     */
    private List<Integer> convertTagIdsToList(String tagIds) {
        List<Integer> list = new ArrayList<>();
        if (!"".equals(tagIds) && tagIds != null) {
            String[] ids = tagIds.split(",");
            for (String id : ids) {
                list.add(Integer.valueOf(id));
            }
        }
        return list;
    }

    @Override
    public Tag getTagByName(String name) {
        Example example = new Example(Tag.class);
        example.createCriteria().andEqualTo("name", name);
        return tagMapper.selectOneByExample(example);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Tag updateTag(Tag tag) {
        Tag t = tagMapper.selectByPrimaryKey(tag);
        if (t == null) {
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(tag, t);
        tagMapper.updateByPrimaryKey(t);
        return t;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteTag(Integer id) {
        int count = 0;
        try {
            count = tagMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Cause exception when delete tag. {}", e.getMessage());
        }
        return count;
    }
}
