package io.grx.modules.sys.service;

import io.grx.modules.sys.entity.SysChannelUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 渠道与审核人员对应关系
 *
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-06 17:58:41
 */
public interface SysChannelUserService {

	SysChannelUserEntity queryObject(Long id);

	List<SysChannelUserEntity> queryList(Map<String, Object> map);

	List<Long> queryChannelUserIdList(Long channelId);

	int queryTotal(Map<String, Object> map);

	void update(SysChannelUserEntity sysChannelUser);

	void delete(Long id);

	void deleteBatch(Long[] ids);

	void saveOrUpdate(Long channelId, List<Long> auditorIdList);

	List<SysChannelUserEntity> queryChannelUserNameIdList(Long channelId);

}
