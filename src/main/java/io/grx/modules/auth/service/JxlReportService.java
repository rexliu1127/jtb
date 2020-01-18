package io.grx.modules.auth.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.auth.entity.JxlReportEntity;

/**
 * 用户认证报告(聚信立)
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-31 21:54:19
 */
public interface JxlReportService {
	
	JxlReportEntity queryObject(Long id);

	List<JxlReportEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(JxlReportEntity jxlReport);
	
	void update(JxlReportEntity jxlReport);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	JxlReportEntity queryByToken(String token);
}
