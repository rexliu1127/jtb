package io.grx.modules.auth.dao;

import io.grx.modules.auth.entity.AuthUserReportDT;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthUserReportDTDao extends BaseDao<AuthUserReportDT> {

    AuthUserReportDT queryByUserId(@Param(value="userId") long userId);
}
