package io.grx.modules.auth.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.auth.entity.AuthUserTokenEntity;

/**
 * 认证用户Token
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:19
 */
public interface AuthUserTokenService {
	
	AuthUserTokenEntity queryObject(Long userId);
	
	List<AuthUserTokenEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AuthUserTokenEntity authUserToken);
	
	void update(AuthUserTokenEntity authUserToken);
	
	void delete(Long userId);
	
	void deleteBatch(Long[] userIds);

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	String createToken(long userId);

	/**
	 * 退出，修改token值
	 * @param userId  用户ID
	 */
	void logout(long userId);

	Long getUserIdByToken(String token);
	
	AuthUserTokenEntity queryByToken(String token);
}
