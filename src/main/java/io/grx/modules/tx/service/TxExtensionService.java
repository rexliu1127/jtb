package io.grx.modules.tx.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.grx.modules.tx.entity.TxExtensionEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.enums.ExtensionStatus;

/**
 * 展期记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-03 00:42:11
 */
public interface TxExtensionService {
	
	TxExtensionEntity queryObject(Long extensionId);
	
	List<TxExtensionEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TxExtensionEntity txExtension);
	
	void update(TxExtensionEntity txExtension);
	
	void delete(Long extensionId);
	
	void deleteBatch(Long[] extensionIds);

	/**
	 * 取出最新的展期
	 * @param txId
	 * @return
	 */
	TxExtensionEntity getLastExtensionByTx(long txId);

	/**
	 * 取出所有展期
	 * @param txId
	 * @return
	 */
	List<TxExtensionEntity> getExtensionsByTx(long txId);

	/**
	 * 修改展期状态
	 * @param status
	 */
	void updateStatus(TxExtensionEntity extensionEntity, ExtensionStatus status, TxUserEntity userEntity);


	BigDecimal getExtensionFee();

	boolean isFreeExtensionLenderMobile(String mobile);
}
