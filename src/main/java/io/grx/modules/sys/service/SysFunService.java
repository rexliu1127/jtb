package io.grx.modules.sys.service;

import io.grx.modules.sys.entity.SysFunEntity;

import java.util.List;
import java.util.Map;

/**
 * 充值表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-13 16:17:35
 */
public interface SysFunService {
	
	SysFunEntity queryObject(Long id);
	
	List<SysFunEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysFunEntity sysFun);
	
	void update(SysFunEntity sysFun);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	void updateRistBatch(Long[] ids);

	SysFunEntity  queryinfo(String merchant_no);
}
