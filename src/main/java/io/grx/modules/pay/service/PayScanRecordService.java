package io.grx.modules.pay.service;

import io.grx.modules.pay.entity.PayScanRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 支付扫描记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-27 16:07:28
 */
public interface PayScanRecordService {
	
	PayScanRecordEntity queryObject(String payId);
	
	List<PayScanRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(PayScanRecordEntity payScanRecord);
	
	void update(PayScanRecordEntity payScanRecord);
	
	void delete(String payId);
	
	void deleteBatch(String[] payIds);
}
