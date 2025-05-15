package com.stp.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "blog_comment")
@Getter
@Setter
@ToString
public class BlogComment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;               // 评论ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blog blog;           // 关联博客的ID

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;           // 关联用户的ID
    private String username;

    private String content; // 评论内容

    @Column(name = "create_time")
    private Date createTime; // 创建时间

    @Column(name = "update_time")
    private Date updateTime; // 更新时间

    private String status; // 评论状态（可用于标记删除等）
}
