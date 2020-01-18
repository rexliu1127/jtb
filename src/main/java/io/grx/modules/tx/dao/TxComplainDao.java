package io.grx.modules.tx.dao;

import org.apache.ibatis.annotations.Mapper;

import io.grx.modules.sys.dao.BaseDao;
import io.grx.modules.tx.entity.TxComplainEntity;

/**
 * 借条申诉
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-05-13 18:10:01
 */
@Mapper
public interface TxComplainDao extends BaseDao<TxComplainEntity> {

    TxComplainEntity getLatestByTxId(Long txId);
}
