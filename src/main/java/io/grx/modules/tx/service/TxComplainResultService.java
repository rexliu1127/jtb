package io.grx.modules.tx.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.tx.entity.TxComplainResultEntity;

/**
 * 借条申诉处理结果
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-05-13 18:10:01
 */
public interface TxComplainResultService {
	
	TxComplainResultEntity queryObject(Long id);
	
	List<TxComplainResultEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TxComplainResultEntity txComplainResult);
	
	void update(TxComplainResultEntity txComplainResult);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	List<TxComplainResultEntity> queryByComplainId(Long complainId);
}
