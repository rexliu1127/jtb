package io.grx.modules.tx.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.grx.common.utils.Query;
import io.grx.modules.tx.dto.TxBaseSummary;
import io.grx.modules.tx.dto.TxMonthSummary;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.wx.dto.TxBorrowerSummary;
import io.grx.wx.dto.TxOverdueSummaryDto;
import io.grx.wx.dto.TxSummaryDto;

/**
 * 交易基本资料
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-11-26 22:41:25
 */
public interface TxBaseService {
	
	TxBaseEntity queryObject(Long txId);

	TxBaseEntity queryByUuid(String txUuid);

	List<TxBaseEntity> queryList(Map<String, Object> map);

	List<TxBaseEntity> getLendingList(Long userId);

    List<TxBaseEntity> getLendingList(Long userId, String userName);

    List<TxBaseEntity> getBorrowingList(Long userId);

    List<TxBaseEntity> getBorrowingList(Long userId, String userName);

    List<TxBaseEntity> getPendingToConfirmList(Long userId);

    List<TxBaseEntity> getPendingToConfirmBorrowingList(Long userId);

    List<TxBaseEntity> getPendingToConfirmLendingList(Long userId);

	int queryTotal(Map<String, Object> map);
	
	void save(TxBaseEntity txBase);
	
	void update(TxBaseEntity txBase);
	
	void delete(Long txId);
	
	void deleteBatch(Long[] txIds);

    /**
     * 待还款总额
     * @param userId
     * @return
     */
    Double sumPendingRepayByMe(Long userId);

    /**
     * 待收款总额
     * @param userId
     * @return
     */
    Double sumPendingRepayToMe(Long userId);

    /**
     * 借款额
     * @param userId
     * @return
     */
    Long sumBorrow(Long userId);

    /**
     * 借款笔数
     * @param userId
     * @return
     */
    Long sumBorrowCount(Long userId);

    /**
     * 总借款利息
     * @param userId
     * @return
     */
    Double sumBorrowInterest(Long userId);

    /**
     * 已出借总额(不含利息)
     * @param userId
     * @return
     */
    Long sumLendedWithoutInterest(Long userId);

    /**
     * 已出借笔数
     * @param userId
     * @return
     */
    Long sumLendedCount(Long userId);

    /**
     * 已出借利息
     * @param userId
     * @return
     */
    Double sumLendedInterest(Long userId);

    /**
     * 我的借条列表
     * @param userId
     * @param lenderName
     * @return
     */
    List<TxBaseEntity> queryBorrowedTransaction(Long userId, String lenderName);

    /**
     * 我的借条列表 (逾期)
     * @param userId
     * @param lenderName
     * @return
     */
    List<TxBaseEntity> queryBorrowedOverdueTransaction(Long userId, String lenderName);

    /**
     * 我的出借列表
     * @param userId
     * @param borrowerName
     * @return
     */
	List<TxBaseEntity> queryLendedTransaction(Long userId, String borrowerName);

    /**
     * 借款信息（借款人列表）
     * @param borrowerUserId
     * @return
     */
    List<TxBaseEntity> queryBorrowerInformation(Long borrowerUserId);

    /**
     * 待收还款总额
     * @param userId
     * @param days X天内待还. X < 0表示全部
     * @return
     */
    Long sumPendingRepayWithoutInterest(Long userId, int days);

    /**
     * 待还款总额
     * @param userId
     * @param days X天内待还. X < 0表示全部
     * @return
     */
    Long sumPendingToPayWithoutInterest(Long userId, int days);

    /**
     * 待收还款总额 (含利息)
     * @param userId
     * @param days X天内待还. X < 0表示全部
     * @return
     */
    Double sumPendingRepayWithInterest(Long userId, int days);

    /**
     * 待还款总额 (含利息)
     * @param userId
     * @param days X天内待还. X < 0表示全部
     * @return
     */
    Double sumPendingToPayWithInterest(Long userId, int days);


    /**
     * 待还款列表
     * @return
     */
    List<TxBaseEntity> queryPendingToPayList(Query query);

    /**
     * 待收款列表
     * @return
     */
    List<TxBaseEntity> queryPendingRepayList(Query query);

    /**
     * 查询借入统计
     * @param userId
     * @return
     */
    TxSummaryDto queryBorrowerSummary(Long userId);

    /**
     * 查询借出统计
     * @param userId
     * @return
     */
    TxSummaryDto queryLenderSummary(Long userId);

    /**
     * 查询逾期统计
     * @param userId
     * @return
     */
    TxOverdueSummaryDto queryOverdueSummary(Long userId);

    long sumPendingToPay(Long userId);
    double sumPaid(Long userId);
    long sumPendingRepay(Long userId);
    double sumRepaid(Long userId);

    String getNewTxUuid();

    List<TxBaseEntity> getExpiredPendingList();

    void updateOverdueTx(Long txId);

    /**
     * 借条费用(系统费用)
     * @return
     */
    BigDecimal getTransactionFee();

    /**
     * 计算借条利息
     * @return
     */
    BigDecimal calculateInterest(TxBaseEntity txBaseEntity);

    TxBorrowerSummary getBorrowerSummary(String idNo, String mobile);

    boolean hasOverdueRecord(Long userId);

    TxBaseEntity queryObjectNoAcl(Long txId);

    void confirmTx(TxBaseEntity txBaseEntity, TxUserEntity user);

    void extendTx(TxBaseEntity txBaseEntity, int extendAmount, Date newEndDate, int newRate);

    boolean isFreeTx(String borrowerIdNo, String lenderName);

    TxBorrowerSummary getBorrowerSummaryByUserId(Long userId);


    TxBaseSummary queryTodaySummary(Map<String, Object> params);

    TxBaseSummary queryHistorySummary(Map<String, Object> params);

    List<TxMonthSummary> queryMonthSummary(Map<String, Object> params);

    boolean isFreeTxLenderMobile(String mobile);

    List<TxBaseEntity>  queryBaseListByIdNo(String idNo);
}
