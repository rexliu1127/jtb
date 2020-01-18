package io.grx.modules.tx.service;

import java.util.List;
import java.util.Map;

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
public interface TxOverdueRecordService {
	
	TxOverdueRecordEntity queryObject(Long id);
	
	List<TxOverdueRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TxOverdueRecordEntity txOverdueRecord);
	
	void update(TxOverdueRecordEntity txOverdueRecord);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	TxOverdueRecordEntity queryLatestByTxId(Long txId);

    TxOverdueSummaryDto queryOverdueSummary(Long userId);

    List<TxOverdueRecordDto> queryByUserId(Long userId);

    void completeOverdueRecord(Long txId);
}
