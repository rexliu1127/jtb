package io.grx.modules.opt.dao;

import io.grx.common.utils.Query;
import io.grx.modules.opt.dto.ChannelStatVO;
import org.apache.ibatis.annotations.Mapper;

import io.grx.modules.opt.entity.ChannelEntity;
import io.grx.modules.sys.dao.BaseDao;

import java.util.List;
import java.util.Map;

/**
 * 渠道
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-11-22 20:19:17
 */
@Mapper
public interface ChannelDao extends BaseDao<ChannelEntity> {

    ChannelEntity queryByKey(String channelKey);

    ChannelEntity queryMerchantDefaultChannel(String merchantNo);

    boolean isDeptHasChannel(Long deptId);
    
    int queryChannelFlowSpreadListTotal(Map<String,Object> params);
    
    List<Map<String,Object>> queryChannelFlowSpreadList(Map<String,Object> params);
    
    List<ChannelEntity> queryFlowChannelList(String merchantNo);

	List<Map<String, Object>> queryMerchantChannelStatis(Query query);

	int queryMerchantChannelStatisCount(Query query);
}
