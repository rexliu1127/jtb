package io.grx.modules.tx.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.tx.dao.TxOverdueRecordDao;
import io.grx.modules.tx.dto.TxOverdueRecordDto;
import io.grx.modules.tx.entity.TxOverdueRecordEntity;
import io.grx.modules.tx.service.TxOverdueRecordService;
import io.grx.wx.dto.TxOverdueSummaryDto;


@Service("txOverdueRecordService")
public class TxOverdueRecordServiceImpl implements TxOverdueRecordService {
	@Autowired
	private TxOverdueRecordDao txOverdueRecordDao;
	
	@Override
	public TxOverdueRecordEntity queryObject(Long id){
		return txOverdueRecordDao.queryObject(id);
	}
	
	@Override
	public List<TxOverdueRecordEntity> queryList(Map<String, Object> map){
		return txOverdueRecordDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map){
		return txOverdueRecordDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(TxOverdueRecordEntity txOverdueRecord){
		txOverdueRecordDao.save(txOverdueRecord);
	}
	
	@Override
	@Transactional
	public void update(TxOverdueRecordEntity txOverdueRecord){
		txOverdueRecordDao.update(txOverdueRecord);
	}

	@Override
	@Transactional
	public void delete(Long id){
		txOverdueRecordDao.delete(id);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		txOverdueRecordDao.deleteBatch(ids);
	}

	@Override
	public TxOverdueRecordEntity queryLatestByTxId(final Long txId) {
		return txOverdueRecordDao.queryLatestByTxId(txId);
	}

	@Override
	public TxOverdueSummaryDto queryOverdueSummary(final Long userId) {
		return txOverdueRecordDao.queryOverdueSummary(userId);
	}

	@Override
	public List<TxOverdueRecordDto> queryByUserId(final Long userId) {
		return txOverdueRecordDao.queryByUserId(userId);
	}

	@Override
	public void completeOverdueRecord(final Long txId) {

		TxOverdueRecordEntity txOverdueRecordEntity = queryLatestByTxId(txId);
		if (txOverdueRecordEntity != null && txOverdueRecordEntity.getOverdueEndDate() == null) {
			txOverdueRecordEntity.setOverdueEndDate(new Date());
			update(txOverdueRecordEntity);
		}
	}

}
