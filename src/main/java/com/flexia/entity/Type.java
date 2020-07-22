package com.flexia.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
    private Integer typeId;

    @NotBlank(message = "分类名称不能为空")
    private String name;

    @Transient
    private Integer blogNum;

    @Transient
    private List<Blog> blogList;
}
