package io.grx.modules.tx.service;

import io.grx.modules.tx.entity.TxUserRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 交易用户关系
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-01 22:44:07
 */
public interface TxUserRelationService {
	
	TxUserRelationEntity queryObject(Long relationId);
	
	List<TxUserRelationEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TxUserRelationEntity txUserRelation);
	
	void update(TxUserRelationEntity txUserRelation);
	
	void delete(Long relationId);
	
	void deleteBatch(Long[] relationIds);
}
