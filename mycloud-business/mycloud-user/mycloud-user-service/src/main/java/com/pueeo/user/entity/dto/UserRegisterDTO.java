package com.pueeo.user.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class UserRegisterDTO implements Serializable {

    /**
     * 账号
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号
     */
    private String phone;
    /**
     * qq唯一标识
     */
    private String qqUnionid;
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
}
