package io.grx.modules.sys.service;

import java.util.List;
import java.util.Map;

import io.grx.common.utils.R;
import io.grx.modules.sys.entity.SysMerchantEntity;

/**
 * 商家
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-06-09 23:41:29
 */
public interface SysMerchantService {
	
	SysMerchantEntity queryObject(String merchantNo);
	
	List<SysMerchantEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysMerchantEntity sysMerchant);
	
	void update(SysMerchantEntity sysMerchant);
	
	void delete(String merchantNo);
	
	void deleteBatch(String[] merchantNos);

	R register(String mobile, String password, String name, String email);

	List<SysMerchantEntity> queryValidList(Map<String, Object> map);

	List<SysMerchantEntity> queryValidListByFunType(Map<String, Object> map);
}
