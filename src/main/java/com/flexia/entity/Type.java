package com.flexia.entity;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

/**
 * @Description 分类实体类
 * @Author hustffx
 * @Date 2020/7/7 0:15
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Transient
    private List<Blog> blogList;
}
