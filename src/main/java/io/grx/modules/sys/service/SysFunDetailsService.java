package io.grx.modules.sys.service;

import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.sys.entity.SysFunDetailsEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户费用明细表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-13 16:17:36
 */
public interface SysFunDetailsService {
	
	SysFunDetailsEntity queryObject(Long id);
	
	List<SysFunDetailsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysFunDetailsEntity sysFunDetails);
	
	void update(SysFunDetailsEntity sysFunDetails);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	SysFunDetailsEntity  queryInfoByTaskID(String taskID);

	int saveFunDetailsByDuoTou(AuthUserEntity authUser);

}
