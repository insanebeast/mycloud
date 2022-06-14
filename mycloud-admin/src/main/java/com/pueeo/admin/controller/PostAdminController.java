package com.pueeo.admin.controller;

import com.pueeo.common.support.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/post")
@Slf4j
public class PostAdminController {

    public ApiResult delete(Long postId){
        log.info("post[{}] has been deleted", postId);
        return ApiResult.success();
    }
}
