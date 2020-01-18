package io.grx.modules.tx.dao;

import org.apache.ibatis.annotations.Mapper;

import io.grx.modules.sys.dao.BaseDao;
import io.grx.modules.tx.entity.TxRepayPlanEntity;

/**
 * 还款计划
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-05-25 22:23:48
 */
@Mapper
public interface TxRepayPlanDao extends BaseDao<TxRepayPlanEntity> {

    TxRepayPlanEntity queryLatestByTx(Long txId);
}
