package com.pueeo.user.mapper;

import com.pueeo.user.entity.UserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户账号表 Mapper 接口
 * </p>
 *
 * @author pueeo
 * @since 2022-05-09
 */
@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount> {

}
