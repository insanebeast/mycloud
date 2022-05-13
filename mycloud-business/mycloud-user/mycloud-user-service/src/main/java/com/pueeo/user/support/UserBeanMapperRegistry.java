package com.pueeo.user.support;

import com.pueeo.common.support.orika.BeanMapperRegistry;
import com.pueeo.user.entity.UserProfile;
import com.pueeo.user.entity.dto.UserRegisterDTO;
import com.pueeo.user.entity.vo.UserProfileVO;
import ma.glasnost.orika.MapperFactory;
import org.springframework.stereotype.Component;

/**
 * User映射规则
 */
@Component
public class UserBeanMapperRegistry implements BeanMapperRegistry {
    @Override
    public void registry(MapperFactory mapperFactory) {
        mapperFactory.classMap(UserProfile.class, UserProfileVO.class)
                .field("nick","nickname")
                .field("avatar", "avatarUrl")
                .byDefault().register();

        mapperFactory.classMap(UserProfile.class, UserRegisterDTO.class)
                .field("nick","nickname")
                .field("avatar", "avatarUrl")
                .byDefault().register();
    }
}