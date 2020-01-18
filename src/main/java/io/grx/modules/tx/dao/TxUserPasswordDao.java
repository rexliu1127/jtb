package io.grx.modules.tx.dao;

import io.grx.modules.tx.entity.TxUserPasswordEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 交易用户密码
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-01 10:41:32
 */
@Mapper
public interface TxUserPasswordDao extends BaseDao<TxUserPasswordEntity> {
	
}
