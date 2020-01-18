package io.grx.modules.tx.service;

import io.grx.modules.tx.entity.TxLenderEntity;

import java.util.List;
import java.util.Map;

/**
 * 出借人
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-06 14:48:57
 */
public interface TxLenderService {
	
	TxLenderEntity queryObject(Long id);
	
	List<TxLenderEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TxLenderEntity txLender);
	
	void update(TxLenderEntity txLender);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	TxLenderEntity queryByMobile(String mobile);

	List<String> getAllLenderMobiles();

	List<TxLenderEntity>  queryListByMerchantNo(Map<String, Object> map);
}
