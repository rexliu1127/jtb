package io.grx.modules.opt.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import io.grx.auth.entity.AuthConfig;
import io.grx.auth.service.YouDunService;
import io.grx.common.utils.CacheUtils;
import io.grx.common.utils.Query;
import io.grx.modules.opt.dto.ChannelStatVO;
import io.grx.modules.sys.service.SysChannelUserService;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.service.SysConfigService;
import io.grx.modules.sys.service.SysDeptService;
import io.grx.modules.sys.service.SysUserService;
import io.grx.modules.tx.utils.TxConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.common.annotation.DataFilter;
import io.grx.common.utils.UUIDGenerator;
import io.grx.modules.opt.dao.ChannelDao;
import io.grx.modules.opt.entity.ChannelEntity;
import io.grx.modules.opt.service.ChannelService;
import io.grx.auth.entity.MachineAuthConfig;

@Service("channelService")
public class ChannelServiceImpl implements ChannelService {
    private static final String KEY_YOUDUN_MOBILE = "YOU_DUN_MOBILE";
    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private SysChannelUserService sysChannelUserService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private YouDunService youDunService;

    private static final String KEY_AUTH_CONFIG = "AUTH_CONFIG";

    private static final String KEY_MACHINE_AUTH_CONFIG = "MACHINE_AUTH_CONFIG";
    @Override
    public ChannelEntity queryObject(Long channelId){
        return channelDao.queryObject(channelId);
    }

    @Override
    @DataFilter(tableAlias = "c", user = false)
    public List<ChannelEntity> queryList(Map<String, Object> map){
        return channelDao.queryList(map);
    }

    @Override
    @DataFilter(tableAlias = "c", user = false)
    public int queryTotal(Map<String, Object> map){
        return channelDao.queryTotal(map);
    }

    @Override
    @Transactional
    public void save(ChannelEntity channel){
        if (StringUtils.isBlank(channel.getChannelKey())) {
            channel.setChannelKey(UUIDGenerator.getShortUUID());
        }
        channelDao.save(channel);
        //保存渠道与审核人员关联关系
        sysChannelUserService.saveOrUpdate(channel.getChannelId(),channel.getAuditorIdList());
    }

    @Override
    @Transactional
    public void update(ChannelEntity channel){
        if (StringUtils.isBlank(channel.getChannelKey())) {
            channel.setChannelKey(UUIDGenerator.getShortUUID());
        }
        channelDao.update(channel);
        //保存渠道与审核人员关联关系
        sysChannelUserService.saveOrUpdate(channel.getChannelId(),channel.getAuditorIdList());
    }

    @Override
    @Transactional
    public void delete(Long channelId){
        channelDao.delete(channelId);
    }

    @Override
    @Transactional
    public void deleteBatch(Long[] channelIds){
        channelDao.deleteBatch(channelIds);
    }

    @Override
    public ChannelEntity queryByKey(final String channelKey) {
        return channelDao.queryByKey(channelKey);
    }

    @Override
    public ChannelEntity queryMerchantDefaultChannel(String merchantNo) {
        return channelDao.queryMerchantDefaultChannel(merchantNo);
    }

    @Override
    public boolean isYouDaoCreditEnabled(ChannelEntity channel) {
        return isYouDaoCreditEnabled(channel.getMerchantNo());
    }

    @Override
    public boolean isYouDaoCreditEnabled(String merchantNo) {
        boolean enabledForAll = youDunService.enabledForAll();
        if (enabledForAll) {
            return Boolean.TRUE;
        }

        List<String> mobiles = sysConfigService.getConfigObject(
                KEY_YOUDUN_MOBILE, ArrayList.class);

        return !CollectionUtils.isEmpty(mobiles) && mobiles.contains(merchantNo);
    }

    @Override
    public boolean isDeptHasChannel(Long deptId) {
        return channelDao.isDeptHasChannel(deptId);
    }

    @Override
    public Long getAssigneeId(ChannelEntity channelEntity) {
        Long nextAssigneeId = null;
        //查询该渠道下指定的审核人
        List<SysUserEntity> auditorList = sysUserService.querySpecifiedFreeAuditorList(channelEntity.getChannelId());

        if (CollectionUtils.isEmpty(auditorList)) {
            // 如果渠道没有指定审核人，测查询公司内部所有未分配有渠道的审核员 (拥有审核员角色)
            auditorList = sysUserService.queryFreeAuditorList(channelEntity.getDeptId());
        }

        auditorList = filterByDeptId(auditorList, channelEntity.getDeptId());

        //获取缓存中最后审核人id
        String key = "auditor_assigned_" + channelEntity.getMerchantNo();
        Long lastAssignedId = NumberUtils.toLong(cacheUtils.get(key), 0L);
        //判断指定审核人列表是否为空，如果为空说明未指定审核人，平均分配给后台的“审核员";如果不为空则说明已经指定了审核人
        if (CollectionUtils.isNotEmpty(auditorList)) {
            if (lastAssignedId >= auditorList.get(auditorList.size() - 1).getUserId()) {
                lastAssignedId = 0L;
            }

            for (SysUserEntity freeAuditor : auditorList) {
                if (freeAuditor.getUserId() > lastAssignedId) {
                    nextAssigneeId = freeAuditor.getUserId();
                    break;
                }
            }
        }
        //最后审核人id放入缓存，下次分配审核员使用
        if (nextAssigneeId != null) {
            cacheUtils.put(key, String.valueOf(nextAssigneeId), 60 * 60 * 24 * 30);
        }
        return nextAssigneeId;
    }

    private List<SysUserEntity> filterByDeptId(List<SysUserEntity> users, Long deptId) {
        List<SysUserEntity> result = new ArrayList<>();
        for (SysUserEntity user : users) {
            if (user != null) {
                if (deptId.equals(user.getDeptId())) {
                    result.add(user);
                } else {
                    List<Long> supDeptIds = sysDeptService.queryDetpIdList(user.getDeptId());
                    if (supDeptIds.contains(deptId)) {
                        result.add(user);
                    }
                }
            }
        }
        return result;
    }


    @Override
    public AuthConfig getAuthConfig() {

        AuthConfig config = sysConfigService.getConfigObject(KEY_AUTH_CONFIG, AuthConfig.class);

        if (config == null) {
            config = new AuthConfig();
        }
        return config;
    }

    @Override
    public MachineAuthConfig getMachineAuthConfig() {

        MachineAuthConfig config = sysConfigService.getConfigObject(KEY_MACHINE_AUTH_CONFIG, MachineAuthConfig.class);

        if (config == null) {
            config = new MachineAuthConfig();
        }
        return config;
    }

    @Override
    public void updateAuthConfig(AuthConfig config) {
        if (config != null) {
            sysConfigService.updateValueByKey(KEY_AUTH_CONFIG, new Gson().toJson(config));
        }
    }

    @Override
    public void updateMachineAuthConfig(MachineAuthConfig config) {
        if (config != null) {
            sysConfigService.updateConfigObject(KEY_MACHINE_AUTH_CONFIG, config);
        }
    }

    @Override
    public int queryChannelFlowSpreadListTotal(Map<String,Object> params) {
    	return this.channelDao.queryChannelFlowSpreadListTotal(params);
    }
    
    @Override
    public List<Map<String,Object>> queryChannelFlowSpreadList(Map<String,Object> params) {
    	return this.channelDao.queryChannelFlowSpreadList(params);
    }
    
    @Override
    public List<ChannelEntity> queryFlowChannelList(String merchantNo){
    	return this.channelDao.queryFlowChannelList(merchantNo);
    }
    
    /**
	 * 获取服务费
	 * 2019-03-24 因为后台还无次功能，服务费统一 20%
	 * @param amount
	 * @param period
	 * @return
	 */
	public BigDecimal getServiceFee(Long amount,Integer period,Long channelId) {
		BigDecimal feeRate = new BigDecimal(0.2);
		return new BigDecimal(amount).multiply(feeRate).setScale(2, BigDecimal.ROUND_DOWN);
	}
	
	/**
	 * 获取利息
	 * 2019-03-24 因后台还无此功能，日息为 0.1%
	 * @param amount
	 * @param period
	 * @return
	 */
	public BigDecimal getInterest(Long amount,Integer period,Long channelId) {
		BigDecimal dayRate = new BigDecimal(0.001);
		return new BigDecimal(amount).multiply(dayRate).multiply(new BigDecimal(period)).setScale(2, BigDecimal.ROUND_DOWN);
	}
	
	public List<Map<String, Object>> queryMerchantChannelStatis(Query query){
		return this.channelDao.queryMerchantChannelStatis(query);
	}

	public int queryMerchantChannelStatisCount(Query query) {
		return this.channelDao.queryMerchantChannelStatisCount(query);
	}
}
