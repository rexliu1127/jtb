package io.grx.modules.auth.dao;

import io.grx.modules.auth.entity.AuthUserBankCard;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthUserBankCardDao extends BaseDao<AuthUserBankCard> {

    AuthUserBankCard queryByUserId(@Param(value="userId") long userId);
}
