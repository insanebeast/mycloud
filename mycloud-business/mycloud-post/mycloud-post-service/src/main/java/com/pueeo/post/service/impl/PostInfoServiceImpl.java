package com.pueeo.post.service.impl;

import com.pueeo.post.entity.PostInfo;
import com.pueeo.post.mapper.PostInfoMapper;
import com.pueeo.post.service.PostInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 帖子基础信息表 服务实现类
 * </p>
 *
 * @author pueeo
 * @since 2022-05-10
 */
@Service
public class PostInfoServiceImpl extends ServiceImpl<PostInfoMapper, PostInfo> implements PostInfoService {

}
