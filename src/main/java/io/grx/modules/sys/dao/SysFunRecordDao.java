package io.grx.modules.sys.dao;

import io.grx.modules.sys.entity.SysFunRecordEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 充值记录表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-13 17:55:46
 */
@Mapper
public interface SysFunRecordDao extends BaseDao<SysFunRecordEntity> {
	
}
