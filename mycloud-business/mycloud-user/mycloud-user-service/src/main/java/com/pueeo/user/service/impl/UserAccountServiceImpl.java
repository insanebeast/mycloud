package com.pueeo.user.service.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pueeo.common.constant.MQOutBinding;
import com.pueeo.common.exception.BusinessException;
import com.pueeo.common.support.ResultEnum;
import com.pueeo.common.utils.BeanUtil;
import com.pueeo.common.utils.SnowflakeIdUtil;
import com.pueeo.user.constant.UserConst;
import com.pueeo.user.entity.UserAccount;
import com.pueeo.user.entity.UserProfile;
import com.pueeo.user.entity.dto.UserRegisterDTO;
import com.pueeo.user.entity.vo.UserProfileVO;
import com.pueeo.user.mapper.UserAccountMapper;
import com.pueeo.user.mq.RocketMQMessageService;
import com.pueeo.user.service.UserAccountService;
import com.pueeo.user.service.UserProfileService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * <p>
 * 用户账号表 服务实现类
 * </p>
 *
 * @author pueeo
 * @since 2022-05-09
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

    @Resource
    private UserProfileService userProfileService;
    @Resource
    private RocketMQMessageService rocketMQMessageService;
    private static final String DEFAULT_AVATAR = "http://img.pueeo.com/default_avatar.png";

    @Override
    public UserProfileVO register(UserRegisterDTO userDTO) {
        String username = userDTO.getUsername();
        Assert.isTrue(StringUtils.isNotBlank(username), "username cannot be blank");

        //username存在则报错
        lambdaQuery().eq(UserAccount::getUsername, username)
                .oneOpt().ifPresent((ex)-> {throw new BusinessException(ResultEnum.USER_ACCOUNT_EXIST);});

        UserAccount userAccount = BeanUtil.map(userDTO, UserAccount.class);
        UserProfile userProfile = BeanUtil.map(userDTO, UserProfile.class);

        //user_account
        long uid = SnowflakeIdUtil.nextId();
        userAccount.setUid(uid);
        userAccount.setStatus(UserConst.AccountStatus.normal);
        save(userAccount);

        //user_profile
        userProfile.setUid(uid);
        userProfile.setAvatar(StringUtils.isBlank(userProfile.getAvatar())? DEFAULT_AVATAR : userProfile.getAvatar());
        userProfileService.save(userProfile);

        //mq
        rocketMQMessageService.send(userProfile, MQOutBinding.USER_REGIST);
        return BeanUtil.map(userProfile, UserProfileVO.class);
    }
}
