package io.grx.modules.sys.service;

import io.grx.modules.sys.entity.SysUserTokenEntity;
import io.grx.common.utils.R;

/**
 * 用户Token
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:22:07
 */
public interface SysUserTokenService {

	SysUserTokenEntity queryByUserId(Long userId);

	void save(SysUserTokenEntity token);
	
	void update(SysUserTokenEntity token);

	 /**
	 * 生成token
	 * @param userId  用户ID
	 */
	R createToken(long userId);

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	R createToken(long userId, long expireInSeconds);

	/**
	 * 退出，修改token值
	 * @param userId  用户ID
	 */
	void logout(long userId);

}
