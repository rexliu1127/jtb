package io.grx.modules.tx.dao;

import java.util.List;
import java.util.Map;

import io.grx.modules.tx.dto.TxBaseSummary;
import io.grx.modules.tx.dto.TxMonthSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.grx.modules.sys.dao.BaseDao;
import io.grx.modules.tx.entity.TxBaseEntity;
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
@Mapper
public interface TxBaseDao extends BaseDao<TxBaseEntity> {

    TxBaseEntity queryByUuid(String txUuid);

    List<TxBaseEntity> getLendingList(@Param(value = "userId") Long userId, @Param(value = "userName") String userName);

    List<TxBaseEntity> getBorrowingList(@Param(value = "userId") Long userId, @Param(value = "userName") String userName);

    List<TxBaseEntity> getPendingToConfirmList(@Param(value = "userId") Long userId);

    List<TxBaseEntity> getPendingToConfirmBorrowingList(@Param(value = "userId") Long userId);

    List<TxBaseEntity> getPendingToConfirmLendingList(@Param(value = "userId") Long userId);

    Double sumBorrowingWithInterest(@Param(value = "userId") Long userId);

    Double sumLendingWithInterest(@Param(value = "userId") Long userId);

    Long sumBorrowedWithoutInterest(@Param(value = "userId") Long userId);

    Long sumBorrowedCount(@Param(value = "userId") Long userId);

    Double sumBorrowInterest(@Param(value = "userId") Long userId);

    List<TxBaseEntity> queryBorrowedList(@Param(value = "userId") Long userId, @Param(value = "userName") String
            userName, @Param(value = "overdue") boolean overdue);

    List<TxBaseEntity> queryLendedList(@Param(value = "userId") Long userId, @Param(value = "userName") String
            userName);

    List<TxBaseEntity> queryBorrowerInformationList(@Param(value = "borrowerUserId") Long borrowerUserId);

    Long sumLendedWithoutInterest(@Param(value = "userId") Long userId);

    Long sumLendedCount(@Param(value = "userId") Long userId);

    Double sumLendedInterest(@Param(value = "userId") Long userId);

    Long sumPendingRepayWithoutInterest(@Param(value = "userId") Long userId, @Param(value = "days") int days);

    Long sumPendingToPayWithoutInterest(@Param(value = "userId") Long userId, @Param(value = "days") int days);

    Double sumPendingRepayWithInterest(@Param(value = "userId") Long userId, @Param(value = "days") int days);

    Double sumPendingToPayWithInterest(@Param(value = "userId") Long userId, @Param(value = "days") int days);

    List<TxBaseEntity> queryPendingToPayList(Map<String, Object> map);

    List<TxBaseEntity> queryPendingRepayList(Map<String, Object> map);

    TxSummaryDto queryBorrowerSummary(@Param(value = "userId") Long userId);

    TxSummaryDto queryLenderSummary(@Param(value = "userId") Long userId);

    TxOverdueSummaryDto queryOverdueSummary(@Param(value = "userId") Long userId);

    long sumPendingToPay(Long userId);
    Double sumPaid(Long userId);
    long sumPendingRepay(Long userId);
    Double sumRepaid(Long userId);

    int getNewTxSeqNo();

    List<TxBaseEntity> getExpiredPendingList();

    void updateOverdueTx();

    TxBorrowerSummary getBorrowerSummary(@Param(value = "idNo") String idNo, @Param(value = "mobile") String mobile);

    TxBorrowerSummary getBorrowerSummaryByUserId(@Param(value = "userId") Long userId);

    boolean hasOverdueRecord(Long userId);

    TxBaseSummary queryTodaySummary(Map<String, Object> params);

    TxBaseSummary queryHistorySummary(Map<String, Object> params);

    List<TxMonthSummary> queryMonthSummary(Map<String, Object> params);

    List<TxBaseEntity>  queryBaseListByIdNo(String idNo);
}
