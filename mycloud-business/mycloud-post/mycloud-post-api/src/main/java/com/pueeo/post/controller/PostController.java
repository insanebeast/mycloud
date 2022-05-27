package com.pueeo.post.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.google.common.collect.ImmutableMap;
import com.pueeo.common.constant.SysConstant;
import com.pueeo.common.exception.BusinessException;
import com.pueeo.common.support.ApiResult;
import com.pueeo.common.support.LoginUser;
import com.pueeo.common.support.redis.RedisService;
import com.pueeo.common.support.sysconf.SysConfigEnum;
import com.pueeo.common.support.sysconf.SysConfigService;
import com.pueeo.post.config.PostSysConfig;
import com.pueeo.post.entity.dto.PostDTO;
import com.pueeo.post.service.PostService;
import com.pueeo.user.config.UserSysConfig;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/post")
public class PostController {

    @Resource
    private DiscoveryClient discoveryClient;
//    @Autowired
//    private RestTemplate restTemplate;
    @Resource
    private PostService postService;
    @Resource
    private RedisService redisService;
    @Resource
    private SysConfigService sysConfigService;



//    @GetMapping("/hello")
//    public String hello(){
//        ServiceInstance instance = null;
//        List<ServiceInstance> instances = discoveryClient.getInstances("mycloud-user");
//        if (!CollectionUtils.isEmpty(instances)){
//            instance = instances.get(0);
//        }
//        if (instance == null){
//            return "找不到服务实例";
//        }
//        String url = instance.getUri() + "/user/get/1001";
//        return restTemplate.getForObject(url, String.class);
//    }

    @GetMapping("/config/get")
    public ApiResult getConfig(){
        PostSysConfig postSysConfig = sysConfigService.getConfig(SysConfigEnum.POST);
        UserSysConfig userSysConfig = sysConfigService.getConfig(SysConfigEnum.USER);
        return ApiResult.success(ImmutableMap.of("post",postSysConfig,"user",userSysConfig));
    }

    @GetMapping("/config/set")
    public ApiResult setConfig(boolean a,boolean b,boolean c){
        PostSysConfig postSysConfig = new PostSysConfig();
        postSysConfig.setPostSwitch(a);
        postSysConfig.setBindPhone(b);
        postSysConfig.setCertificate(c);
        sysConfigService.updateConfig(SysConfigEnum.POST, postSysConfig);
        UserSysConfig userSysConfig = new UserSysConfig();
        userSysConfig.setEnableForceCertificate(a);
        userSysConfig.setEnableForceBindPhone(b);
        sysConfigService.updateConfig(SysConfigEnum.USER, userSysConfig);
        return ApiResult.success();
    }

    @GetMapping("/add")
    public ApiResult add(PostDTO postDTO){
        postService.add(postDTO);
        return ApiResult.success();
    }

    @GetMapping("/get")
    public ApiResult get(Long id){
        return ApiResult.success(postService.get(id));
    }

    @GetMapping("/info")
    public ApiResult info(HttpServletRequest request) {
        LoginUser loginUser = (LoginUser)request.getAttribute(SysConstant.USER_CONTEXT_REQUEST_ATTRIBUTE);
        LoginUser loginUser1 = postService.info();
        Assert.isTrue(loginUser1.equals(loginUser), "not equals");
        return ApiResult.success(loginUser1);
    }

    @GetMapping("/degrade")
    @SentinelResource(value = "post.degrade", blockHandler = "blockHandler",  fallback = "fallback")
    public ApiResult degrade(int id){
        if (id == 9){
            throw new BusinessException(400, "id illegal");
        }
        if (id== 0){
            throw new IllegalArgumentException("id = 0");
        }
        return ApiResult.success("success:"+id);
    }

    /**
     * 仅针对BlockException
     * @param id
     * @param exception
     * @return
     */
    public static ApiResult blockHandler(int id, BlockException exception){
        return ApiResult.success("block:"+ id);
    }

    /**
     * 针对所有异常
     * @param id
     * @param throwable
     * @return
     */
    public static ApiResult fallback(int id, Throwable throwable){
        return ApiResult.success("fallback:"+ id);
    }


}
