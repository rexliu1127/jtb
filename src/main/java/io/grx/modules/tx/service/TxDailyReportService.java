package io.grx.modules.tx.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.tx.dto.TxStatSum;
import io.grx.modules.tx.entity.TxDailyReportEntity;

/**
 * 每日借条统计
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-01-27 22:50:18
 */
public interface TxDailyReportService {
	
	TxDailyReportEntity queryObject(String reportDate);
	
	List<TxDailyReportEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);

	TxStatSum queryStatSum();

    void generateLastDaySum();
}
