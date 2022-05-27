package com.pueeo.post;

import com.pueeo.common.support.sysconf.SysConfigEnum;
import com.pueeo.common.support.sysconf.SysConfigService;
import com.pueeo.post.config.PostSysConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostApplicationTests {

    @Autowired
    private SysConfigService sysConfigService;

    @Test
    void contextLoads() {
    }

    @Test
    void testConfig() {
        PostSysConfig postSysConfig = new PostSysConfig();
        postSysConfig.setPostSwitch(true);
        postSysConfig.setBindPhone(true);
        postSysConfig.setCertificate(false);
        sysConfigService.updateConfig(SysConfigEnum.POST, postSysConfig);

        System.out.println("==============");
        PostSysConfig config = sysConfigService.getConfig(SysConfigEnum.POST);
        System.out.println(config);

    }

}
