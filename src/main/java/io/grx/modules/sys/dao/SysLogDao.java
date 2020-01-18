package io.grx.modules.sys.dao;

import io.grx.modules.sys.entity.SysLogEntity;

import org.apache.ibatis.annotations.Mapper;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 * 系统日志
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-08 10:40:56
 */
@Mapper
public interface SysLogDao extends BaseDao<SysLogEntity> {
    List<SysLogEntity> queryListByMerchantNo(Map<String, Object> map);

    int queryTotalByMerchantNo(Map<String, Object> map);
}
