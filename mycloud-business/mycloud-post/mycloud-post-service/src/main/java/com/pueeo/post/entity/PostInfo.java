package com.pueeo.post.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 帖子基础信息表
 * </p>
 *
 * @author pueeo
 * @since 2022-05-10
 */
@Getter
@Setter
@TableName("post_info")
public class PostInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 帖子id
     */
    @TableId("id")
    private Long id;

    /**
     * 用户uid
     */
    @TableField("uid")
    private Long uid;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 类型 1-文字 2-图片 3-视频
     */
    @TableField("post_type")
    private Integer postType;

    /**
     * 状态 0-审核中 1-审核通过 2-审核不通过 3-用户删除 4-违规删除 
     */
    @TableField("post_status")
    private Integer postStatus;

    /**
     * 视频链接
     */
    @TableField("video_url")
    private String videoUrl;

    /**
     * 视频封面
     */
    @TableField("video_cover")
    private String videoCover;

    /**
     * 图片链接，多张用,分隔
     */
    @TableField("image_url")
    private String imageUrl;

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
