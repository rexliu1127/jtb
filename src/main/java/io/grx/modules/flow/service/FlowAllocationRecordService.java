package io.grx.modules.flow.service;


import java.util.List;
import java.util.Map;

import io.grx.modules.flow.entity.FlowAllocationRecordEntity;

/**
 * 用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:22:06
 */
public interface FlowAllocationRecordService {

	FlowAllocationRecordEntity queryObject(Long userId);
	
	List<FlowAllocationRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	FlowAllocationRecordEntity save(FlowAllocationRecordEntity entity);
	
	void update(FlowAllocationRecordEntity entity);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
	
	public Integer queryConcurrentCountByRequestId(Long sourceRequestId);
	
	public List<Map<String,Object>> queryAllocationStatis(Map<String,Object> params);
	
	public Integer queryAllocationStatisTotal(Map<String,Object> params);
	
	public List<Map<String,Object>> querySpreadList(Map<String,Object> params);
	public Integer querySpreadListTotal(Map<String,Object> params);
	
	public List<FlowAllocationRecordEntity> queryFollowDraftList(String time);
	
	/**
	 * 跟踪报告
	 */
	public void trackFlowRequest();
	
	/**
	 * 流量分发用户跟踪
	 * @param requestId
	 */
	public void updateTrackAuthUser(Long requestId);
	
	/**
	 * 流量分发报告跟踪
	 * @param requestId
	 */
	public void updateTrackAuthUserRequest(Long requestId);

}
