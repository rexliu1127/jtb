package io.grx.modules.auth.service.impl;

import io.grx.modules.auth.dao.BqsReportDao;
import io.grx.modules.auth.entity.BqsReportEntity;
import io.grx.modules.auth.enums.BaiqishiType;
import io.grx.modules.auth.service.BqsReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;




@Service("bqsReportService")
public class BqsReportServiceImpl implements BqsReportService {
	@Autowired
	private BqsReportDao bqsReportDao;
	
	@Override
	public BqsReportEntity queryObject(Long id){
		return bqsReportDao.queryObject(id);
	}
	
	@Override
	public List<BqsReportEntity> queryList(Map<String, Object> map){
		return bqsReportDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return bqsReportDao.queryTotal(map);
	}
	
	@Override
	public void save(BqsReportEntity bqsReport){
		bqsReportDao.save(bqsReport);
	}
	
	@Override
	public void update(BqsReportEntity bqsReport){
		bqsReportDao.update(bqsReport);
	}
	
	@Override
	public void delete(Long id){
		bqsReportDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		bqsReportDao.deleteBatch(ids);
	}
	@Override
	public BqsReportEntity queryLatestByUserId(final Long userId, final BaiqishiType baiqishiType) {
		return bqsReportDao.queryLatestByUserId(userId, baiqishiType);
	}

	@Override
	public BqsReportEntity queryByUniqueId(final String uniqueId) {
		return bqsReportDao.queryByUniqueId(uniqueId);
	}

	@Override
	public List<BqsReportEntity> queryObjectByCondition(Map<String, Object> map){
		return bqsReportDao.queryObjectByCondition(map);
	}
}
