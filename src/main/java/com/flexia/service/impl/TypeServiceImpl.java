package com.flexia.service.impl;

import com.flexia.entity.Type;
import com.flexia.exception.NotFoundException;
import com.flexia.mapper.TypeMapper;
import com.flexia.service.TypeService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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
    public Type getType(Long id) {
        return typeMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<Type> listType() {
        return (Page<Type>) typeMapper.selectAll();
    }

    @Override
    public Type getTypeByName(String typename) {
        Example example = new Example(Type.class);
        example.createCriteria().andEqualTo("name", typename);
        return typeMapper.selectOneByExample(example);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeMapper.selectByPrimaryKey(id);
        if (t == null) {
            throw new NotFoundException("不存在该类型");
        }
        typeMapper.updateByPrimaryKey(type);
        return typeMapper.selectByPrimaryKey(type.getTypeId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteType(Long id) {
        typeMapper.deleteByPrimaryKey(id);
    }
}
