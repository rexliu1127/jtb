package io.grx.modules.tx.service;

import io.grx.common.utils.R;
import io.grx.modules.tx.dto.TxUserWithdrawalVO;
import io.grx.modules.tx.entity.TxUserWithdrawEntity;
import io.grx.modules.tx.enums.WithdrawalStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户提现记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-07-29 18:10:26
 */
public interface TxUserWithdrawService {
	
	TxUserWithdrawEntity queryObject(Long id);
	
	List<TxUserWithdrawEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TxUserWithdrawEntity txUserWithdraw);
	
	void update(TxUserWithdrawEntity txUserWithdraw);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	void withdraw(Long userId, BigDecimal amount, BigDecimal feeAmount);

    double sumUserWithdrawal(Long userId);

    List<TxUserWithdrawalVO> queryAdminList(Map<String, Object> params);

    R updateStatus(Long txUserWithdrawalId, WithdrawalStatus status);
}
