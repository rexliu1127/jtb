package io.grx.modules.flow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.grx.modules.flow.entity.FlowSettingEntity;
import io.grx.modules.sys.dao.BaseDao;

@Mapper
public interface FlowSettingDao extends BaseDao<FlowSettingEntity> {

	public FlowSettingEntity queryLastSettingByMerchantNo(@Param("merchantNo")String merchantNo);
	
	public List<FlowSettingEntity> queryOpenSettingFlow();
}
