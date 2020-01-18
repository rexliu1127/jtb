package io.grx.modules.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.grx.modules.auth.entity.YxReportEntity;
import io.grx.modules.auth.enums.YiXiangType;
import io.grx.modules.sys.dao.BaseDao;

/**
 * 亿象认证
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-04-17 17:39:13
 */
@Mapper
public interface YxReportDao extends BaseDao<YxReportEntity> {

    YxReportEntity queryByTaskId(String taskId);

    YxReportEntity queryLatestByUserId(@Param(value = "userId") Long userId,
                                       @Param(value = "type") YiXiangType type);

    int expireReports(@Param(value = "type") YiXiangType type, @Param(value = "expiredDay") int expiredDay);
}
