package com.pueeo.post.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class Post {
    private Long id;
    private Long uid;
    private String content;
    private Date createTime;
}
