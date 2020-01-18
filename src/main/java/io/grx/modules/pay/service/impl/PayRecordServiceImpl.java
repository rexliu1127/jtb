package io.grx.modules.pay.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.pay.dao.PayRecordDao;
import io.grx.modules.pay.entity.PayRecordEntity;
import io.grx.modules.pay.service.PayRecordService;



@Service("payRecordService")
public class PayRecordServiceImpl implements PayRecordService {
	@Autowired
	private PayRecordDao payRecordDao;
	
	@Override
	public PayRecordEntity queryObject(Long id){
		return payRecordDao.queryObject(id);
	}
	
	@Override
	public List<PayRecordEntity> queryList(Map<String, Object> map){
		return payRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return payRecordDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(PayRecordEntity payRecord){
		payRecordDao.save(payRecord);
	}
	
	@Override
	@Transactional
	public void update(PayRecordEntity payRecord){
		payRecordDao.update(payRecord);
	}
	
	@Override
	@Transactional
	public void delete(Long id){
		payRecordDao.delete(id);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		payRecordDao.deleteBatch(ids);
	}

	@Override
	public PayRecordEntity queryByTrxId(final String trxId) {
		return payRecordDao.queryByTrxId(trxId);
	}

	@Override
	public long sumPaidAmount(final Date startTme, final Date endTime) {
		return payRecordDao.sumPaidAmount(startTme, endTime);
	}

}
