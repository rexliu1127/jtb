package io.grx.modules.tx.service;

import io.grx.modules.tx.entity.*;

import java.math.BigDecimal;

/**
 * 用户余额
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-12 11:08:17
 */
public interface TxUserBalanceService {
	
	TxUserBalanceEntity queryObject(Long userId);

	void addBalanceByDeletingTx(TxBaseEntity txBaseEntity);

	boolean useBalanceByPayTx(TxBaseEntity txBaseEntity);

    boolean useBalanceByPayExtension(TxExtensionEntity txExtensionEntity);

	void addBalanceByReward(TxUserRewardEntity userRewardEntity);

	void updateBalanceByLog(TxUserBalanceLogEntity userBalanceLogEntity);

	BigDecimal getWithdrawalFeeRate();

	BigDecimal getWithdrawalMinFee();

	BigDecimal getWithdrawalMaxFee();

    BigDecimal getWithdrawalMinAmount();

}
