package io.grx.modules.opt.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.qiniu.util.Auth;
import io.grx.auth.entity.AuthConfig;
import io.grx.modules.opt.dto.ChannelStatVO;
import io.grx.modules.opt.entity.ChannelEntity;
import io.grx.auth.entity.MachineAuthConfig;
import io.grx.common.utils.Query;

/**
 * 渠道
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-11-22 20:19:17
 */
public interface ChannelService {
	
	ChannelEntity queryObject(Long channelId);
	
	List<ChannelEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ChannelEntity channel);

	void update(ChannelEntity channel);

	void delete(Long channelId);

	void deleteBatch(Long[] channelIds);

	ChannelEntity queryByKey(String channelKey);

	ChannelEntity queryMerchantDefaultChannel(String merchantNo);

	boolean isYouDaoCreditEnabled(ChannelEntity channel);

	boolean isYouDaoCreditEnabled(String merchantNo);

	boolean isDeptHasChannel(Long deptId);

	Long getAssigneeId(ChannelEntity channelEntity);

	AuthConfig getAuthConfig();
	MachineAuthConfig getMachineAuthConfig();

	void updateAuthConfig(AuthConfig config);
	void updateMachineAuthConfig(MachineAuthConfig config);
	
	int queryChannelFlowSpreadListTotal(Map<String,Object> params);
	
	List<Map<String,Object>> queryChannelFlowSpreadList(Map<String,Object> params);
	
	List<ChannelEntity> queryFlowChannelList(String merchantNo);
	
	/**
	 * 获取服务费
	 * 2019-03-24 因为后台还无次功能，服务费统一 20%
	 * @param amount
	 * @param period
	 * @return
	 */
	BigDecimal getServiceFee(Long amount,Integer period,Long channelId);
	
	/**
	 * 获取利息
	 * 2019-03-24 因后台还无此功能，日息为 0.1%
	 * @param amount
	 * @param period
	 * @return
	 */
	BigDecimal getInterest(Long amount,Integer period,Long channelId);

	List<Map<String, Object>> queryMerchantChannelStatis(Query query);

	int queryMerchantChannelStatisCount(Query query);
}
