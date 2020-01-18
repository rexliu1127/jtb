package io.grx.modules.auth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.grx.modules.auth.entity.AuthUserReportDsEntity;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.sys.dao.BaseDao;

/**
 * 电商认证
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
@Mapper
public interface AuthUserReportDsDao extends BaseDao<AuthUserReportDsEntity> {

    AuthUserReportDsEntity queryByTaskId(String taskId);

    List<AuthUserReportDsEntity> queryByStatuses(@Param(value = "userId") Long userId,
                                                 @Param(value = "verifyStatuses") VerifyStatus... verifyStatuses);
}
