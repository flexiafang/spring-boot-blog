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

    Type getTypeById(Long id);

    Page<Type> listType();

    Type getTypeByName(String name);

    Type updateType(Type type);

    int deleteType(Long id);
}