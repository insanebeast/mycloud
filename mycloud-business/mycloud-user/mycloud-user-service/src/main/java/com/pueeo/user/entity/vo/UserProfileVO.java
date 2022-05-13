package com.pueeo.user.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class UserProfileVO {

    /**
     * 用户id
     */
    private Long uid;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatarUrl;
    /**
     * 性别(0:女 | 1:男 | 2:未知)
     */
    private Integer gender;
    /**
     * 生日
     */
    private LocalDate birthday;
    /**
     * 个性签名
     */
    private String description;
}
