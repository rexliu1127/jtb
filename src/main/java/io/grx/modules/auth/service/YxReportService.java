package io.grx.modules.auth.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.auth.entity.YxReportEntity;
import io.grx.modules.auth.enums.YiXiangType;

/**
 * 亿象认证
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-04-17 17:39:13
 */
public interface YxReportService {
	
	YxReportEntity queryObject(Long id);
	
	List<YxReportEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(YxReportEntity yxReport);
	
	void update(YxReportEntity yxReport);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	YxReportEntity queryByTaskId(String taskId);

	YxReportEntity queryLatestByUserId(Long userId, YiXiangType type);

	void expireReports();
}
