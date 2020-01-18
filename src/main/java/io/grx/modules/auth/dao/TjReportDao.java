package io.grx.modules.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.grx.modules.auth.entity.TjReportEntity;
import io.grx.modules.auth.enums.TianjiType;
import io.grx.modules.sys.dao.BaseDao;

/**
 * 天机认证
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-04-13 14:52:36
 */
@Mapper
public interface TjReportDao extends BaseDao<TjReportEntity> {

    TjReportEntity queryByUniqueId(String uniqueId);

    TjReportEntity queryLatestByUserId(@Param(value = "userId") Long userId,
                                       @Param(value = "tianjiType") TianjiType tianjiType);

    int expireReports(@Param(value = "type") TianjiType type, @Param(value = "expiredDay") int expiredDay);

	TjReportEntity querySuccessReportByTaskId(@Param("taskId")String taskId,@Param(value = "tianjiType")TianjiType tianjiType);
}
