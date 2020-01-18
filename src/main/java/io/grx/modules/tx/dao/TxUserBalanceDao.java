package io.grx.modules.tx.dao;

import org.apache.ibatis.annotations.Mapper;

import io.grx.modules.sys.dao.BaseDao;
import io.grx.modules.tx.entity.TxUserBalanceEntity;

/**
 * 用户余额
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-12 11:08:17
 */
@Mapper
public interface TxUserBalanceDao extends BaseDao<TxUserBalanceEntity> {

    TxUserBalanceEntity getAndLock(Long userId);
}
