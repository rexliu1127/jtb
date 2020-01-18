package io.grx.modules.tx.service;

import io.grx.modules.tx.dto.TxUserInviteVO;
import io.grx.modules.tx.entity.TxUserInviteEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户邀请记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-07-22 14:34:31
 */
public interface TxUserInviteService {
	
	TxUserInviteEntity queryObject(Long id);
	
	List<TxUserInviteEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TxUserInviteEntity txUserInvite);
	
	void update(TxUserInviteEntity txUserInvite);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	List<TxUserInviteVO> queryUserInviteList(Long userId, Integer level);

    TxUserInviteEntity queryByUseId(Long userId);

	int getTeamUserCount(Long userId);
}
