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
 * @Description 评论实体类
 * @Author hustffx
 * @Date 2020/7/7 0:15
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nickname;
    private String email;
    private String content;
    private String avatar;
    private Date createTime;
    private Integer blogId;
    private Integer parentCommentId;
    @Transient
    private List<Comment> replyComments;
}
