package io.grx.modules.tx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.grx.modules.tx.entity.TxRepaymentEntity;
import io.grx.modules.sys.dao.BaseDao;

/**
 * 还款记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-03 00:42:11
 */
@Mapper
public interface TxRepaymentDao extends BaseDao<TxRepaymentEntity> {

    /**
     * 取出待确认还款记录
     * @param txId
     * @return
     */
    TxRepaymentEntity getLastRepaymentByTx(Long txId);

    List<TxRepaymentEntity> queryListByLender(@Param(value = "userId") Long userId,
                                              @Param(value = "userName") String userName);


    List<TxRepaymentEntity> queryListByBorrower(@Param(value = "userId") Long userId,
                                                @Param(value = "userName") String userName);

    List<TxRepaymentEntity> queryListByTx(@Param(value = "txId") Long txId);
}
