package com.pueeo.post.mapper;

import com.pueeo.post.entity.PostInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 帖子基础信息表 Mapper 接口
 * </p>
 *
 * @author pueeo
 * @since 2022-05-10
 */
@Mapper
public interface PostInfoMapper extends BaseMapper<PostInfo> {

}
