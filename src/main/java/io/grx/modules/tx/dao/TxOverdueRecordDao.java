package io.grx.modules.tx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.grx.modules.sys.dao.BaseDao;
import io.grx.modules.tx.dto.TxOverdueRecordDto;
import io.grx.modules.tx.entity.TxOverdueRecordEntity;
import io.grx.wx.dto.TxOverdueSummaryDto;

/**
 * 逾期记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-11 11:36:11
 */
@Mapper
public interface TxOverdueRecordDao extends BaseDao<TxOverdueRecordEntity> {
    TxOverdueRecordEntity queryLatestByTxId(Long txId);

    List<TxOverdueRecordDto> queryByUserId(Long userId);

    TxOverdueSummaryDto queryOverdueSummary(Long userId);

    void insertByOverdueTx(Long txId);
}
