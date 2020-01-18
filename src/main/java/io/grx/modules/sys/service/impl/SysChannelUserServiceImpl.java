package io.grx.modules.sys.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grx.modules.sys.dao.SysChannelUserDao;
import io.grx.modules.sys.entity.SysChannelUserEntity;
import io.grx.modules.sys.service.SysChannelUserService;



@Service("sysChannelUserService")
public class SysChannelUserServiceImpl implements SysChannelUserService {
	@Autowired
	private SysChannelUserDao sysChannelUserDao;

	@Override
	public List<Long> queryChannelUserIdList(final Long channelId) {
		return sysChannelUserDao.queryChannelUserIdList(channelId);
	}
	@Override
	public List<SysChannelUserEntity> queryChannelUserNameIdList(final Long channelId) {
		return sysChannelUserDao.queryChannelUserNameIdList(channelId);
	}
	@Override
	public SysChannelUserEntity queryObject(Long id){
		return sysChannelUserDao.queryObject(id);
	}
	
	@Override
	public List<SysChannelUserEntity> queryList(Map<String, Object> map){
		return sysChannelUserDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysChannelUserDao.queryTotal(map);
	}
	
	@Override
	public void update(SysChannelUserEntity sysChannelUser){
		sysChannelUserDao.update(sysChannelUser);
	}
	
	@Override
	public void delete(Long id){
		sysChannelUserDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		sysChannelUserDao.deleteBatch(ids);
	}
	@Override
	public void saveOrUpdate(final Long channelId, final List<Long> auditorIdList) {
		//先删除渠道与审核人关系
		sysChannelUserDao.delete(channelId);
		//如果审核人列表为空则只进行删除渠道与审核人关系
		if(CollectionUtils.isEmpty(auditorIdList)){
			return ;
		}

		//渠道与审核人关系
		Map<String, Object> map = new HashMap<>();
		map.put("channelId", channelId);
		map.put("auditorIdList", auditorIdList);
		sysChannelUserDao.save(map);
	}
}
