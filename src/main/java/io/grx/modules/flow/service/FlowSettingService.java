package io.grx.modules.flow.service;


import java.util.List;
import java.util.Map;

import io.grx.modules.flow.entity.FlowSettingEntity;

/**
 * 用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:22:06
 */
public interface FlowSettingService {

	FlowSettingEntity queryObject(Long userId);
	
	List<FlowSettingEntity> queryList(Map<String, Object> map);
	
	public List<FlowSettingEntity> queryOpenSettingFlow();
	
	int queryTotal(Map<String, Object> map);
	
	FlowSettingEntity save(FlowSettingEntity entity);
	
	FlowSettingEntity queryLastSettingByMerchantNo(String merchantNo);
	
	void update(FlowSettingEntity entity);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

}
