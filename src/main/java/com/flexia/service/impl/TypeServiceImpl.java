package com.flexia.service.impl;

import com.flexia.entity.Type;
import com.flexia.exception.NotFoundException;
import com.flexia.mapper.TypeMapper;
import com.flexia.service.TypeService;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.net.http.HttpRequest;

/**
 * @Author hustffx
 * @Date 2020/7/14
 */
@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeMapper typeMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Type saveType(Type type) {
        int count = typeMapper.insert(type);
        return count == 1 ? type : null;
    }

    @Override
    public Type getTypeById(Long id) {
        return typeMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<Type> listType() {
        return (Page<Type>) typeMapper.selectAll();
    }

    @Override
    public Type getTypeByName(String name) {
        Example example = new Example(Type.class);
        example.createCriteria().andEqualTo("name", name);
        return typeMapper.selectOneByExample(example);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Type updateType(Type type) {
        Type t = typeMapper.selectByPrimaryKey(type);
        if (t == null) {
            throw new NotFoundException("不存在该分类");
        }
        BeanUtils.copyProperties(type, t);
        typeMapper.updateByPrimaryKey(t);
        return t;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteType(Long id) {
        int count = 0;
        try {
            count = typeMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Cause exception when delete type. {}", e.getMessage());
        }
        return count;
    }
}
