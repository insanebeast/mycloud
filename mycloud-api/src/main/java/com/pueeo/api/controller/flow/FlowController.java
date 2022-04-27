package com.pueeo.api.controller.flow;

import com.pueeo.flow.service.FlowService;
import com.pueeo.user.entity.dto.UserDTO;
import com.pueeo.user.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/flow")
public class FlowController {

    @Resource
    private DiscoveryClient discoveryClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private FlowService flowService;


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
    public String add(Long uid, String content){
        return flowService.add(uid, content);
    }
}
