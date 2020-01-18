package io.grx.modules.tx.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.tx.entity.TxUserEntity;

/**
 * 系统用户
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-11-26 00:49:35
 */
public interface TxUserService {
	
	TxUserEntity queryObject(Long userId);

	TxUserEntity queryByWechatId(String weChatId);

	TxUserEntity queryByMobile(String mobile);

	TxUserEntity queryByUnionId(String unionId);

	List<TxUserEntity> queryByIdNo(String idNo);
	
	List<TxUserEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TxUserEntity txUser);
	
	void update(TxUserEntity txUser);
	
	void delete(Long userId);
	
	void deleteBatch(Long[] userIds);

	List<TxUserEntity> getFriendList(Long userId, String userName);

	List<TxUserEntity> getUserListByMerchantNo(Map<String, Object> map);

	int getFriendTotal(Long userId);

	boolean isFriend(Long userId, Long otherUserId);
}
