package com.stp.domain;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content; // 评论内容

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")  // 外键列，指向 ScenicSpot 的 id
    private ScenicSpot scenicSpot; // 与 ScenicSpot 实体关联

    @ManyToOne
    @JoinColumn(name = "user_id")  // 外键列，指向 ScenicSpot 的 id
    private User user; // 与 ScenicSpot 实体关联

    private String username;

    @Column(name = "created_at")
    private Date createdAt;

}
