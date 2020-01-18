package io.grx.modules.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.grx.modules.sys.dao.SysUserChannelDao;
import io.grx.modules.sys.service.SysUserChannelService;



@Service("sysUserChannelService")
public class SysUserChannelServiceImpl implements SysUserChannelService {
	@Autowired
	private SysUserChannelDao sysUserChannelDao;

	@Override
	public List<Long> queryUserChannelIdList(final Long userId) {
		return sysUserChannelDao.queryUserChannelIdList(userId);
	}

	@Override
	public void saveOrUpdate(final Long userId, final List<Long> channelIdList) {
		if(CollectionUtils.isEmpty(channelIdList)){
			return ;
		}

		//先删除用户与角色关系
		sysUserChannelDao.delete(userId);

		//保存用户与角色关系
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("channelIdList", channelIdList);
		sysUserChannelDao.save(map);
	}

	@Override
	public void delete(final Long userId) {
		sysUserChannelDao.delete(userId);
	}

}
