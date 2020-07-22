package com.flexia.entity;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Description 标签实体类
 * @Author hustffx
 * @Date 2020/7/7 22:37
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;

    @NotBlank(message = "标签不能为空")
    private String name;

    @Transient
    private Integer blogNum;

    @Transient
    private List<Blog> blogList;
}
