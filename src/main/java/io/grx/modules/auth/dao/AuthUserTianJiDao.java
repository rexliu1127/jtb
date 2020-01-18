package io.grx.modules.auth.dao;

import io.grx.modules.auth.entity.AuthUserTianJi;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthUserTianJiDao extends BaseDao<AuthUserTianJi> {

    AuthUserTianJi queryByUserId(@Param(value="userId") long userId);


}
