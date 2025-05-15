package com.stp.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "blog")
@ToString
@Getter
@Setter
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Column(name = "author_id")
    private Integer authorId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private String status;

    @Column(name = "image_url")
    private String imageUrl;

    private String authorName;

    // 构造方法


    public Blog() {
    }

    public Blog(Long id, String title, String content, Integer authorId, Date createTime, Date updateTime, String status, String imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.status = status;
        this.imageUrl = imageUrl;
    }

}