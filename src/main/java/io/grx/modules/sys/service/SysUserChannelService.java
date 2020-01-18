package io.grx.modules.sys.service;

import java.util.List;

/**
 * 渠道用户与渠道对应关系
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-16 23:57:03
 */
public interface SysUserChannelService {

	void delete(Long id);

	List<Long> queryUserChannelIdList(Long userId);

	void saveOrUpdate(Long userId, List<Long> channelIdList);
}
