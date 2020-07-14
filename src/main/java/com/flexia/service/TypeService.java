package com.flexia.service;

import com.flexia.entity.Type;
import com.github.pagehelper.Page;

/**
 * @Description 分类的业务层接口
 * @Author hustffx
 * @Date 2020/7/14
 */
public interface TypeService {

    Type saveType(Type type);

    Type getType(Long id);

    Page<Type> listType();

    Type getTypeByName(String typename);

    Type updateType(Long id, Type type);

    void deleteType(Long id);
}
