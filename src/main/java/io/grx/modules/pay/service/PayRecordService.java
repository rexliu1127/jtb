package io.grx.modules.pay.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.grx.modules.pay.entity.PayRecordEntity;

/**
 * 支付记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-08 01:07:58
 */
public interface PayRecordService {
	
	PayRecordEntity queryObject(Long id);
	
	List<PayRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(PayRecordEntity payRecord);
	
	void update(PayRecordEntity payRecord);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	PayRecordEntity queryByTrxId(String trxId);

	long sumPaidAmount(Date startTme, Date endTime);
}
