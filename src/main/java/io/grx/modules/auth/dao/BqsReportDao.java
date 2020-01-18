package io.grx.modules.auth.dao;

import io.grx.modules.auth.entity.BqsReportEntity;
import io.grx.modules.auth.enums.BaiqishiType;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2019-01-24 10:41:24
 */
@Mapper
public interface BqsReportDao extends BaseDao<BqsReportEntity> {
    BqsReportEntity queryLatestByUserId(@Param(value = "userId") Long userId,
                                       @Param(value = "baiqishiType") BaiqishiType baiqishiType);

    BqsReportEntity queryByUniqueId(String uniqueId);

    List<BqsReportEntity> queryObjectByCondition(Map<String, Object> map);
}
