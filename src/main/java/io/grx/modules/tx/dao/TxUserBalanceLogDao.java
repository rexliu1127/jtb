package io.grx.modules.tx.dao;

import io.grx.modules.tx.entity.TxUserBalanceLogEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户余额记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-12 11:08:17
 */
@Mapper
public interface TxUserBalanceLogDao extends BaseDao<TxUserBalanceLogEntity> {
	
}
