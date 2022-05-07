package com.pueeo.post.entity.vo;

import com.pueeo.user.entity.dto.UserDTO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class PostVO {

    private Long id;
    private UserDTO author;
    private String content;
    private Date createTime;
}
