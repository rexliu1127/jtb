package io.grx.modules.auth.dao;

import io.grx.modules.auth.entity.AuthUserOCREntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthUserOCRDao extends BaseDao<AuthUserOCREntity> {

    AuthUserOCREntity queryByUserId(@Param(value="userId") long userId);
}
