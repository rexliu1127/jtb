package io.grx.modules.pay.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.pay.dao.PayScanRecordDao;
import io.grx.modules.pay.entity.PayScanRecordEntity;
import io.grx.modules.pay.service.PayScanRecordService;



@Service("payScanRecordService")
public class PayScanRecordServiceImpl implements PayScanRecordService {
	@Autowired
	private PayScanRecordDao payScanRecordDao;
	
	@Override
	public PayScanRecordEntity queryObject(String payId){
		return payScanRecordDao.queryObject(payId);
	}
	
	@Override
	public List<PayScanRecordEntity> queryList(Map<String, Object> map){
		return payScanRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return payScanRecordDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(PayScanRecordEntity payScanRecord){
		payScanRecordDao.save(payScanRecord);
	}
	
	@Override
	@Transactional
	public void update(PayScanRecordEntity payScanRecord){
		payScanRecordDao.update(payScanRecord);
	}

	@Override
	@Transactional
	public void delete(String payId){
		payScanRecordDao.delete(payId);
	}
	
	@Override
	@Transactional
	public void deleteBatch(String[] payIds){
		payScanRecordDao.deleteBatch(payIds);
	}
	
}
