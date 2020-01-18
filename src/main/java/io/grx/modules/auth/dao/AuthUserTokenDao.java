package io.grx.modules.auth.dao;

import org.apache.ibatis.annotations.Mapper;

import io.grx.modules.auth.entity.AuthUserTokenEntity;
import io.grx.modules.sys.dao.BaseDao;

/**
 * 认证用户Token
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:19
 */
@Mapper
public interface AuthUserTokenDao extends BaseDao<AuthUserTokenEntity> {

    AuthUserTokenEntity queryByToken(String token);

    AuthUserTokenEntity queryByUser(Long userId);
}
