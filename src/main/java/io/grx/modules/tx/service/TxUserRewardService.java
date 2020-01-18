package io.grx.modules.tx.service;

import io.grx.modules.tx.dto.TxUserRewardStatVO;
import io.grx.modules.tx.dto.TxUserRewardSum;
import io.grx.modules.tx.dto.TxUserRewardVO;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxExtensionEntity;
import io.grx.modules.tx.entity.TxUserRewardEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户奖励
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-07-22 21:24:07
 */
public interface TxUserRewardService {
	
	TxUserRewardEntity queryObject(Long id);
	
	List<TxUserRewardEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TxUserRewardEntity txUserReward);
	
	void update(TxUserRewardEntity txUserReward);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	void addUserReward(TxBaseEntity txBaseEntity);

    void addUserReward(TxExtensionEntity txExtensionEntity);

	List<TxUserRewardVO> queryListByUser(Map<String, Object> params);

    double sumUserReward(Long userId);

    List<TxUserRewardStatVO> queryRewardStat(Map<String, Object> params);

    int queryRewardStatTotal(Map<String, Object> params);

    TxUserRewardSum queryRewardSum();
}
