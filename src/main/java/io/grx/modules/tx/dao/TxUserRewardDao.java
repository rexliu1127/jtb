package io.grx.modules.tx.dao;

import io.grx.modules.tx.dto.TxUserRewardStatVO;
import io.grx.modules.tx.dto.TxUserRewardSum;
import io.grx.modules.tx.dto.TxUserRewardVO;
import io.grx.modules.tx.entity.TxUserRewardEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用户奖励
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-07-22 21:24:07
 */
@Mapper
public interface TxUserRewardDao extends BaseDao<TxUserRewardEntity> {
	List<TxUserRewardVO> queryListByUser(Map<String, Object> params);

	double sumUserReward(Long userId);

	List<TxUserRewardStatVO> queryRewardStat(Map<String, Object> params);

    int queryRewardStatTotal(Map<String, Object> params);

    TxUserRewardSum queryRewardSum();
}
