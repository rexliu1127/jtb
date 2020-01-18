package io.grx.modules.auth.service;


import io.grx.modules.auth.entity.BqsReportEntity;
import io.grx.modules.auth.enums.BaiqishiType;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2019-01-24 10:41:24
 */
public interface BqsReportService {
	
	BqsReportEntity queryObject(Long id);
	
	List<BqsReportEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(BqsReportEntity bqsReport);
	
	void update(BqsReportEntity bqsReport);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
	BqsReportEntity queryLatestByUserId(Long userId, BaiqishiType baiqishiType);

	BqsReportEntity queryByUniqueId(String uniqueId);

	List<BqsReportEntity> queryObjectByCondition(Map<String, Object> map);
}
