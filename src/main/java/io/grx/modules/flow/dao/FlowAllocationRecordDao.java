package io.grx.modules.flow.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.grx.modules.flow.entity.FlowAllocationRecordEntity;
import io.grx.modules.sys.dao.BaseDao;

@Mapper
public interface FlowAllocationRecordDao extends BaseDao<FlowAllocationRecordEntity> {

	public Integer queryConcurrentCountByRequestId(@Param("sourceRequestId")Long sourceRequestId);
	
	public List<Map<String,Object>> queryAllocationStatis(Map<String,Object> params);
	
	public Integer queryAllocationStatisTotal(Map<String,Object> params);
	
	public List<Map<String,Object>> querySpreadList(Map<String,Object> params);
	
	public Integer querySpreadListTotal(Map<String,Object> params);
	
	public List<FlowAllocationRecordEntity> queryFollowDraftList(@Param("createTime")String createTime);
	
	public List<FlowAllocationRecordEntity> queryListBySourceRequestId(@Param("sourceRequestId")Long sourceRequestId);
}
