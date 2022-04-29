package com.pueeo.api.controller.post;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.google.common.collect.ImmutableMap;
import com.pueeo.common.exception.ServiceException;
import com.pueeo.common.support.ApiResult;
import com.pueeo.post.config.MediaResourceConfiguration;
import com.pueeo.post.entity.dto.PostDTO;
import com.pueeo.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/post")
public class PostController {

    @Resource
    private DiscoveryClient discoveryClient;
    @Autowired
    private RestTemplate restTemplate;
    @Resource
    private PostService postService;
    @Autowired
    private MediaResourceConfiguration mediaResourceConfiguration;


    @GetMapping("/hello")
    public String hello(){
        ServiceInstance instance = null;
        List<ServiceInstance> instances = discoveryClient.getInstances("mycloud-user");
        if (!CollectionUtils.isEmpty(instances)){
            instance = instances.get(0);
        }
        if (instance == null){
            return "找不到服务实例";
        }
        String url = instance.getUri() + "/user/get/1001";
        return restTemplate.getForObject(url, String.class);
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
    public ApiResult info() {
        ImmutableMap<String, String> map = ImmutableMap.of("video", mediaResourceConfiguration.getVideo(),
                "image", mediaResourceConfiguration.getImage());
        return ApiResult.success(map);
    }

    @GetMapping("/degrade")
    @SentinelResource(value = "post.degrade", blockHandler = "blockHandler",  fallback = "fallback")
    public ApiResult degrade(int id){
        if (id == 9){
            throw new ServiceException(400, "id illegal");
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
