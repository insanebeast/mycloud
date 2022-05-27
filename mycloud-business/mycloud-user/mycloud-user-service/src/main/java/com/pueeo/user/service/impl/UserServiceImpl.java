package com.pueeo.user.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.nacos.common.utils.StringUtils;
import com.pueeo.common.constant.MQOutBinding;
import com.pueeo.common.exception.BusinessException;
import com.pueeo.common.support.LoginUser;
import com.pueeo.common.support.ResultEnum;
import com.pueeo.common.support.UserContext;
import com.pueeo.common.utils.BeanMapper;
import com.pueeo.common.utils.SnowflakeIdUtil;
import com.pueeo.user.constant.UserConst;
import com.pueeo.user.entity.UserAccount;
import com.pueeo.user.entity.UserProfile;
import com.pueeo.user.entity.dto.UserRegisterDTO;
import com.pueeo.user.entity.vo.UserProfileVO;
import com.pueeo.user.mq.RocketMQMessageService;
import com.pueeo.user.service.UserAccountService;
import com.pueeo.user.service.UserProfileService;
import com.pueeo.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@DubboService(protocol = "dubbo")
@Service
@Slf4j
//@CacheConfig(cacheNames = "USER")
public class UserServiceImpl implements UserService {

    @Resource
    private UserProfileService userProfileService;
    @Resource
    private UserAccountService userAccountService;
    @Resource
    private RocketMQMessageService rocketMQMessageService;

    private static final String DEFAULT_AVATAR = "http://img.pueeo.com/default_avatar.png";


    @SentinelResource(value = "user.get")
    @Cacheable(cacheNames = "USER", key = "#uid")
    public UserProfileVO get(Long uid){
        UserProfile userProfile = userProfileService.getById(uid);
        log.info("get user from db [{}]", uid);
        ResultEnum.USER_NOT_EXIST.notNull(userProfile);
        return BeanMapper.map(userProfile, UserProfileVO.class);
    }

    @Override
    public UserProfileVO register(UserRegisterDTO userDTO) {
        String username = userDTO.getUsername();
        Assert.isTrue(StringUtils.isNotBlank(username), "username cannot be blank");

        //username存在则报错
        userAccountService.lambdaQuery().eq(UserAccount::getUsername, username)
                .oneOpt().ifPresent((ex)-> {throw new BusinessException(ResultEnum.USER_ACCOUNT_EXIST);});

        UserAccount userAccount = BeanMapper.map(userDTO, UserAccount.class);
        UserProfile userProfile = BeanMapper.map(userDTO, UserProfile.class);

        //user_account
        long uid = SnowflakeIdUtil.nextId();
        userAccount.setUid(uid);
        userAccount.setStatus(UserConst.AccountStatus.normal);
        userAccountService.save(userAccount);

        //user_profile
        userProfile.setUid(uid);
        userProfile.setAvatar(StringUtils.isBlank(userProfile.getAvatar())? DEFAULT_AVATAR : userProfile.getAvatar());
        userProfileService.save(userProfile);

        //mq
        rocketMQMessageService.send(userProfile, MQOutBinding.USER_REGIST);
        return BeanMapper.map(userProfile, UserProfileVO.class);
    }

    @Override
    public LoginUser currentUser() {
        LoginUser loginUser = UserContext.get();
        return loginUser;
    }


//
//    public static ApiResult handleException(BlockException exception) {
//        return ApiResult.fail(300, String.format("应用【%s】触发【流量控制】",exception.getRuleLimitApp()));
//    }

}
