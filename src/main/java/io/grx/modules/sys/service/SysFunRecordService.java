package io.grx.modules.sys.service;

import io.grx.modules.sys.entity.SysFunRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 充值记录表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-13 17:55:46
 */
public interface SysFunRecordService {
	
	SysFunRecordEntity queryObject(Long id);
	
	List<SysFunRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysFunRecordEntity sysFunRecord);
	
	void update(SysFunRecordEntity sysFunRecord);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
