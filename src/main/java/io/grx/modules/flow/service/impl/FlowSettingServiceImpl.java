package io.grx.modules.flow.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.flow.dao.FlowSettingDao;
import io.grx.modules.flow.entity.FlowSettingEntity;
import io.grx.modules.flow.service.FlowSettingService;



@Service("flowSettingService")
public class FlowSettingServiceImpl implements FlowSettingService {
	@Autowired
	private FlowSettingDao flowSettingDao;
	
	@Override
	public FlowSettingEntity queryObject(Long userId){
		return flowSettingDao.queryObject(userId);
	}
	
	public List<FlowSettingEntity> queryOpenSettingFlow(){
		return this.flowSettingDao.queryOpenSettingFlow();
	}
	
	@Override
	public List<FlowSettingEntity> queryList(Map<String, Object> map){
		return flowSettingDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return flowSettingDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public FlowSettingEntity save(FlowSettingEntity entity){
		this.flowSettingDao.save(entity);
		return entity;
	}
	
	@Override
	@Transactional
	public void update(FlowSettingEntity entity){
		this.flowSettingDao.update(entity);
	}

	@Override
	@Transactional
	public void delete(Long id){
		flowSettingDao.delete(id);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		flowSettingDao.deleteBatch(ids);
	}

	@Override
	public FlowSettingEntity queryLastSettingByMerchantNo(String merchantNo) {
		return this.flowSettingDao.queryLastSettingByMerchantNo(merchantNo);
	}

}
