package io.grx.modules.tx.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.tx.entity.TxUserTokenEntity;

/**
 * 借条用户Token
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-27 22:13:13
 */
public interface TxUserTokenService {
	
	TxUserTokenEntity queryObject(Long userId);
	
	List<TxUserTokenEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TxUserTokenEntity txUserToken);
	
	void update(TxUserTokenEntity txUserToken);
	
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
}
