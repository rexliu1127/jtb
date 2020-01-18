package io.grx.modules.tx.service.impl;

import static java.time.temporal.ChronoUnit.DAYS;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.grx.modules.tx.dto.TxBaseSummary;
import io.grx.modules.tx.dto.TxMonthSummary;
import io.grx.modules.tx.entity.*;
import io.grx.modules.tx.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.common.exception.RRException;
import io.grx.common.utils.CharUtils;
import io.grx.common.utils.DateUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.sys.service.SysConfigService;
import io.grx.modules.tx.dao.TxBaseDao;
import io.grx.modules.tx.dao.TxOverdueRecordDao;
import io.grx.modules.tx.enums.TxStatus;
import io.grx.modules.tx.utils.TxConstants;
import io.grx.wx.dto.TxBorrowerSummary;
import io.grx.wx.dto.TxOverdueSummaryDto;
import io.grx.wx.dto.TxSummaryDto;
import io.jsonwebtoken.lang.Assert;


@Service("txBaseService")
public class TxBaseServiceImpl implements TxBaseService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TxBaseDao txBaseDao;

    @Autowired
    private TxOverdueRecordDao txOverdueRecordDao;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private TxUserService txUserService;

    @Autowired
    private TxUserRelationService txUserRelationService;

    @Autowired
    private TxMessageService txMessageService;

    @Autowired
    private TxRepayPlanService txRepayPlanService;

    @Autowired
    private AuthRequestService authRequestService;

    @Autowired
    private TxUserRewardService txUserRewardService;

    @Override
    public TxBaseEntity queryObject(Long txId){
        return checkAcl(txBaseDao.queryObject(txId));
    }

    @Override
    public TxBaseEntity queryByUuid(final String txUuid) {
        return checkAcl(txBaseDao.queryByUuid(txUuid));
    }

    @Override
    public List<TxBaseEntity> queryList(Map<String, Object> map){
        return txBaseDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map){
        return txBaseDao.queryTotal(map);
    }

    @Override
    public void save(TxBaseEntity txBase){
        txBaseDao.save(txBase);
    }

    @Override
    public void update(TxBaseEntity txBase){
        txBaseDao.update(txBase);
    }

    @Override
    public void delete(Long txId){
        txBaseDao.delete(txId);
    }

    @Override
    public void deleteBatch(Long[] txIds){
        txBaseDao.deleteBatch(txIds);
    }

    @Override
    public Double sumPendingRepayByMe(final Long userId) {
        return txBaseDao.sumBorrowingWithInterest(userId);
    }

    @Override
    public Double sumPendingRepayToMe(final Long userId) {
        return txBaseDao.sumLendingWithInterest(userId);
    }

    @Override
    public Long sumBorrow(final Long userId) {
        return txBaseDao.sumBorrowedWithoutInterest(userId);
    }

    @Override
    public Long sumBorrowCount(final Long userId) {
        return txBaseDao.sumBorrowedCount(userId);
    }

    @Override
    public Double sumBorrowInterest(final Long userId) {
        return txBaseDao.sumBorrowInterest(userId);
    }
    @Override
    public List<TxBaseEntity>  queryBaseListByIdNo(String idNo){
        return  txBaseDao.queryBaseListByIdNo(idNo);
    }

    /**
     * 已出借总额(不含利息)
     *
     * @param userId
     * @return
     */
    @Override
    public Long sumLendedWithoutInterest(final Long userId) {
        return txBaseDao.sumLendedWithoutInterest(userId);
    }

    /**
     * 已出借笔数
     *
     * @param userId
     * @return
     */
    @Override
    public Long sumLendedCount(final Long userId) {
        return txBaseDao.sumLendedCount(userId);
    }

    /**
     * 已出借利息
     *
     * @param userId
     * @return
     */
    @Override
    public Double sumLendedInterest(final Long userId) {
        return txBaseDao.sumLendedInterest(userId);
    }

    @Override
    public List<TxBaseEntity> queryBorrowedTransaction(final Long userId, final String lenderName) {
        return txBaseDao.queryBorrowedList(userId, lenderName, false);
    }


    @Override
    public List<TxBaseEntity> queryBorrowedOverdueTransaction(final Long userId, final String lenderName) {
        return txBaseDao.queryBorrowedList(userId, lenderName, true);
    }

    @Override
    public List<TxBaseEntity> queryLendedTransaction(final Long userId, final String borrowerName) {
        return txBaseDao.queryLendedList(userId, borrowerName);
    }
    @Override
    public List<TxBaseEntity> queryBorrowerInformation(final Long borrowerUserId) {
        return txBaseDao.queryBorrowerInformationList(borrowerUserId);
    }

    /**
     * 待收还款总额
     *
     * @param userId
     * @param days   X天内待还. X < 0表示全部
     * @return
     */
    @Override
    public Long sumPendingRepayWithoutInterest(final Long userId, final int days) {
        return txBaseDao.sumPendingRepayWithoutInterest(userId, days);
    }

    /**
     * 待还款总额
     *
     * @param userId
     * @param days   X天内待还. X < 0表示全部
     * @return
     */
    @Override
    public Long sumPendingToPayWithoutInterest(final Long userId, final int days) {
        return txBaseDao.sumPendingToPayWithoutInterest(userId, days);
    }

    /**
     * 待收还款总额 (含利息)
     *
     * @param userId
     * @param days   X天内待还. X < 0表示全部
     * @return
     */
    @Override
    public Double sumPendingRepayWithInterest(final Long userId, final int days) {
        return txBaseDao.sumPendingRepayWithInterest(userId, days);
    }

    /**
     * 待还款总额 (含利息)
     *
     * @param userId
     * @param days   X天内待还. X < 0表示全部
     * @return
     */
    @Override
    public Double sumPendingToPayWithInterest(final Long userId, final int days) {
        return txBaseDao.sumPendingToPayWithInterest(userId, days);
    }

    /**
     * 待还款列表
     *
     * @param userId
     * @param userName 搜索出借人姓名
     * @return
     */
    @Override
    public List<TxBaseEntity> queryPendingToPayList(Query query) {
        query.put("isLetter", CharUtils.isAllLetters((String) query.get("userName")));
        return txBaseDao.queryPendingToPayList(query);
    }

    /**
     * 待收款列表
     *
     * @param userId
     * @param userName 搜索出借款人姓名
     * @return
     */
    @Override
    public List<TxBaseEntity> queryPendingRepayList(Query query) {
        query.put("isLetter", CharUtils.isAllLetters((String) query.get("userName")));
        return txBaseDao.queryPendingRepayList(query);
    }

    /**
     * 查询借入统计
     *
     * @param userId
     * @return
     */
    @Override
    public TxSummaryDto queryBorrowerSummary(final Long userId) {
        return txBaseDao.queryBorrowerSummary(userId);
    }

    /**
     * 查询借出统计
     *
     * @param userId
     * @return
     */
    @Override
    public TxSummaryDto queryLenderSummary(final Long userId) {
        return txBaseDao.queryLenderSummary(userId);
    }

    /**
     * 查询逾期统计
     *
     * @param userId
     * @return
     */
    @Override
    public TxOverdueSummaryDto queryOverdueSummary(final Long userId) {
        return txBaseDao.queryOverdueSummary(userId);
    }

    @Override
    public long sumPendingToPay(final Long userId) {
        return txBaseDao.sumPendingToPay(userId);
    }

    @Override
    public double sumPaid(final Long userId) {
        return txBaseDao.sumPaid(userId);
    }

    @Override
    public long sumPendingRepay(final Long userId) {
        return txBaseDao.sumPendingRepay(userId);
    }

    @Override
    public double sumRepaid(final Long userId) {
        return txBaseDao.sumRepaid(userId);
    }

    @Override
    public String getNewTxUuid() {
        int seq = txBaseDao.getNewTxSeqNo();
        return DateUtils.formateDate(new Date(), "yyyyMMddHHmmssS")
                + StringUtils.leftPad(String.valueOf(seq), 3, '0');
    }

    @Override
    public List<TxBaseEntity> getExpiredPendingList() {
        return txBaseDao.getExpiredPendingList();
    }

    @Override
    @Transactional
    public void updateOverdueTx(Long txId) {
//        txOverdueRecordDao.insertByOverdueTx(txId);
        txBaseDao.updateOverdueTx();
    }

    /**
     * 借条费用(系统费用)
     *
     * @return
     */
    @Override
    public BigDecimal getTransactionFee() {
        String feeStr = sysConfigService.getValue(TxConstants.TRANSACTION_FEE);
        if (StringUtils.isBlank(feeStr)) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(feeStr);
        }
    }

    /**
     * 计算借条利息
     *
     * @return
     */
    @Override
    public BigDecimal calculateInterest(TxBaseEntity txBaseEntity) {
        LocalDate beginLocalDate = txBaseEntity.getBeginDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = txBaseEntity.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        /* 一年按365天计算, 借款当天也计算利息*/
        return new BigDecimal(txBaseEntity.getAmount()).multiply(new BigDecimal(txBaseEntity.getRate()))
                .multiply(new BigDecimal(String.valueOf(DAYS.between(beginLocalDate, endLocalDate) + 1)))
                .divide(new BigDecimal("36500"), 2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public TxBorrowerSummary getBorrowerSummary(final String idNo, final String mobile) {
        if (StringUtils.isBlank(idNo) && StringUtils.isBlank(mobile)) {
            return null;
        }
        return txBaseDao.getBorrowerSummary(idNo, mobile);
    }

    @Override
    public boolean hasOverdueRecord(final Long userId) {
        return txBaseDao.hasOverdueRecord(userId);
    }

    @Override
    public TxBaseEntity queryObjectNoAcl(final Long txId) {
        return txBaseDao.queryObject(txId);
    }

    @Override
    public void confirmTx(final TxBaseEntity entity, TxUserEntity user) {

        if (entity.getBorrowerUserId() == null) {
            entity.setBorrowerUserId(user.getUserId());
            entity.setBorrowerSignImgPath(user.getSignImgPath());
        } else if (entity.getLenderUserId() == null) {
            entity.setLenderUserId(user.getUserId());
            entity.setLenderSignImgPath(user.getSignImgPath());
        }

        entity.setStatus(TxStatus.CONFIRMED);
        entity.setOutstandingAmount(entity.getAmount());
        entity.setOutstandingInterest(entity.getInterest());
        update(entity);

        // create repay plan
        txRepayPlanService.createRepayPlanForTx(entity);

        if (!txUserService.isFriend(entity.getBorrowerUserId(), entity
                .getLenderUserId())) {
            TxUserRelationEntity relationEntity = new TxUserRelationEntity();
            relationEntity.setUserId(entity.getBorrowerUserId());
            relationEntity.setFriendUserId(entity.getLenderUserId());
            txUserRelationService.save(relationEntity);

            relationEntity = new TxUserRelationEntity();
            relationEntity.setUserId(entity.getLenderUserId());
            relationEntity.setFriendUserId(entity.getBorrowerUserId());
            txUserRelationService.save(relationEntity);
        }

        // 推广奖励
        txUserRewardService.addUserReward(entity);

        try {
            txMessageService.sendMsgForConfirmingTransaction(entity);
        } catch (Throwable t) {
            logger.warn("failed to send tx message for confirm tx", t);
        }
    }

    @Override
    public void extendTx(final TxBaseEntity baseEntity, final int extendAmount, final Date newEndDate, final int newRate) {
        Assert.isTrue(extendAmount <= baseEntity.getOutstandingAmount());
        baseEntity.setOutstandingAmount(extendAmount);
        baseEntity.setRate(newRate);

        Date oldEndDate = baseEntity.getEndDate();
        baseEntity.setEndDate(newEndDate);

        TxBaseEntity tempBaseEntity = new TxBaseEntity();
        tempBaseEntity.setAmount(baseEntity.getOutstandingAmount());
        tempBaseEntity.setBeginDate(oldEndDate);
        tempBaseEntity.setEndDate(newEndDate);
        tempBaseEntity.setRate(newRate);

        BigDecimal outstandingInterest = calculateInterest(tempBaseEntity);
        baseEntity.setOutstandingInterest(outstandingInterest);
        baseEntity.setInterest(baseEntity.getInterest().add(outstandingInterest));

        if (baseEntity.getStatus() == TxStatus.DELAYED && baseEntity.getEndDate().getTime()
                >= Date.from(LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()) {
            baseEntity.setStatus(TxStatus.CONFIRMED);
        }

        update(baseEntity);
    }

    @Override
    public boolean isFreeTx(final String borrowerIdNo, final String lenderName) {
        List<String> freeTxLenderNames = sysConfigService.getConfigObject(
                TxConstants.FREE_TX_LENDER_NAME, ArrayList.class);
        if (freeTxLenderNames.contains(lenderName)) {
            List<Number> deptNumbers = sysConfigService.getConfigObject(TxConstants.FREE_TX_DEPT_ID, ArrayList.class);
            if (deptNumbers.size() > 0) {
                List<Long> deptIds = deptNumbers.stream().map(e -> e.longValue()).collect(Collectors.toList());
                int recentDays = NumberUtils.toInt(sysConfigService.getValue(TxConstants.FREE_TX_RECENT_AUTH_DAY),
                        0);
                return authRequestService.hasAuthRequest(borrowerIdNo, deptIds, recentDays);
            }
        }
        return false;
    }

    @Override
    public List<TxBaseEntity> getLendingList(final Long userId) {
        return txBaseDao.getLendingList(userId, null);
    }

    @Override
    public List<TxBaseEntity> getLendingList(final Long userId, String userName) {
        return txBaseDao.getLendingList(userId, userName);
    }

    @Override
    public List<TxBaseEntity> getBorrowingList(final Long userId) {
        return txBaseDao.getBorrowingList(userId, null);
    }

    @Override
    public List<TxBaseEntity> getBorrowingList(final Long userId, String userName) {
        return txBaseDao.getBorrowingList(userId, userName);
    }

    @Override
    public List<TxBaseEntity> getPendingToConfirmList(final Long userId) {
        return txBaseDao.getPendingToConfirmList(userId);
    }

    @Override
    public List<TxBaseEntity> getPendingToConfirmBorrowingList(final Long userId) {
        return txBaseDao.getPendingToConfirmBorrowingList(userId);
    }

    @Override
    public List<TxBaseEntity> getPendingToConfirmLendingList(final Long userId) {
        return txBaseDao.getPendingToConfirmLendingList(userId);
    }

    private TxBaseEntity checkAcl(TxBaseEntity baseEntity) {
        if (baseEntity == null) {
            return null;
        }

        if (ShiroUtils.getUserEntity() != null) {
            return baseEntity;
        }

        TxUserEntity user = ShiroUtils.getTxUser();
        if (user == null) {
            throw new RRException("对不起, 您没有权限操作", 401);
        }

        if (baseEntity.getStatus() == TxStatus.NEW || baseEntity.getStatus() == TxStatus.REJECTED
                || baseEntity.getStatus() == TxStatus.UNPAID) {
            if (StringUtils.isNotBlank(user.getName())
                    && !StringUtils.equals(user.getName(), baseEntity.getLenderName())
                    && !StringUtils.equals(user.getName(), baseEntity.getBorrowerName())) {
//				throw new RRException("借条名字与当前用户姓名不匹配", 401);
            }
        } else if (!user.getUserId().equals(baseEntity.getLenderUserId()) && !user.getUserId().equals(baseEntity
                .getBorrowerUserId())) {
            throw new RRException("对不起, 您没有权限操作", 401);
        }
        return baseEntity;
    }


    @Override
    public TxBorrowerSummary getBorrowerSummaryByUserId(final Long userId) {
        return txBaseDao.getBorrowerSummaryByUserId(userId);
    }

    @Override
    public TxBaseSummary queryTodaySummary(Map<String, Object> params) {
        return txBaseDao.queryTodaySummary(params);
    }

    @Override
    public TxBaseSummary queryHistorySummary(Map<String, Object> params) {
        return txBaseDao.queryHistorySummary(params);
    }

    @Override
    public List<TxMonthSummary> queryMonthSummary(Map<String, Object> params) {
        return txBaseDao.queryMonthSummary(params);
    }

    @Override
    public boolean isFreeTxLenderMobile(String mobile) {
        List<String> freeTxLenderMobiles = sysConfigService.getConfigObject(
                TxConstants.FREE_TX_LENDER_MOBILE, ArrayList.class);

        return freeTxLenderMobiles.contains(mobile);
    }
}
