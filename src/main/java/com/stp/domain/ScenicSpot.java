package com.stp.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.annotations.Result;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "scenic_spots")
@ToString
@Getter
@Setter
public class ScenicSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 景点ID

    private String name; // 景点名称

    @Column(name = "ticket_price")
    private BigDecimal ticketPrice; // 票价

    @Column(name = "open_time")
    private String openTime; // 开放时间

    private String description; // 景点描述

    @Column(name = "image_url")
    private String imageUrl; // 景点图片URL

    private Date createdAt;

    private Date updatedAt;

    private String text;

    private Double latitude; // 纬度

    private Double longitude; // 经度

    // 一对多关系：一个景点可以有多个评论
    @OneToMany(mappedBy = "scenicSpot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments; // 景点的评论列表

}
