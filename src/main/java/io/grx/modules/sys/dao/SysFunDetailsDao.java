package io.grx.modules.sys.dao;

import io.grx.modules.sys.entity.SysFunDetailsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户费用明细表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-13 16:17:36
 */
@Mapper
public interface SysFunDetailsDao extends BaseDao<SysFunDetailsEntity> {

    SysFunDetailsEntity  queryInfoByTaskID(String taskID);

	
}
