package io.grx.modules.sys.dao;

import io.grx.modules.sys.entity.SysFunTypeEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 充值类型表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-13 16:17:36
 */
@Mapper
public interface SysFunTypeDao extends BaseDao<SysFunTypeEntity> {
    int queryCountByConditions(Map<String, Object> map);
    SysFunTypeEntity queryObjectByConditions(Map<String, Object> map);
    void saveFunTypeList(List<SysFunTypeEntity> sysFunTypeList);
    List<SysFunTypeEntity> queryInfo(String merchant_no);
}
