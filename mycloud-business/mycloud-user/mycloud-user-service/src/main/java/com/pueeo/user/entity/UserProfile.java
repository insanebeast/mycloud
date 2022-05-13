package com.pueeo.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户资料表
 * </p>
 *
 * @author pueeo
 * @since 2022-05-09
 */
@Getter
@Setter
@TableName("user_profile")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId("uid")
    private Long uid;

    /**
     * 昵称
     */
    @TableField("nick")
    private String nick;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 性别(0:女 | 1:男 | 2:未知)
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 生日
     */
    @TableField("birthday")
    private LocalDate birthday;

    /**
     * 个性签名
     */
    @TableField("description")
    private String description;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除 0-未删除 1-已删除
     */
    @TableField("is_deleted")
    private Boolean isDeleted;


}
