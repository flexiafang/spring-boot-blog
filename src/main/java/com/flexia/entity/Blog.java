package com.flexia.entity;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 博客实体类
 * @Author hustffx
 * @Date 2020/7/6 23:25
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogId;

    private String title;
    private String content;
    private String firstPicture;
    private String flag;
    private String description;

    private Integer views;
    private Boolean appreciation;
    private Boolean shareStatement;
    private Boolean comment;
    private Boolean publish;
    private Boolean recommend;

    private Date createTime;
    private Date updateTime;

    private Long typeId;
    private Long userId;

    @Transient
    private Type type;
    @Transient
    private User user;
    @Transient
    private List<Tag> tags;
    @Transient
    private List<Comment> comments;
}
