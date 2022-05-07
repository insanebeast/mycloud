package com.pueeo.post.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PostDTO {

    private Long uid;
    private String content;
}
