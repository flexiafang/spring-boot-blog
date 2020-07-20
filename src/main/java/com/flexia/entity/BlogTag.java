package com.flexia.entity;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Author hustffx
 * @Date 2020/7/20
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlogTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer blogTagId;
    private Integer blogId;
    private Integer tagId;
}
