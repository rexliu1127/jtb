package io.grx.modules.tx.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.tx.dao.TxDailyReportDao;
import io.grx.modules.tx.dto.TxStatSum;
import io.grx.modules.tx.entity.TxDailyReportEntity;
import io.grx.modules.tx.service.TxDailyReportService;



@Service("txDailyReportService")
public class TxDailyReportServiceImpl implements TxDailyReportService {
	@Autowired
	private TxDailyReportDao txDailyReportDao;
	
	@Override
	public TxDailyReportEntity queryObject(String reportDate){
		return txDailyReportDao.queryObject(reportDate);
	}
	
	@Override
	public List<TxDailyReportEntity> queryList(Map<String, Object> map){
		return txDailyReportDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return txDailyReportDao.queryTotal(map);
	}

	@Override
	public TxStatSum queryStatSum() {
		return txDailyReportDao.queryStatSum();
	}

	@Override
	@Transactional
	public void generateLastDaySum() {
		txDailyReportDao.generateLastDaySum();
	}

}
