package io.grx.modules.job.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.grx.common.utils.DateUtils;
import io.grx.modules.pay.dao.PayRecordDao;
import io.grx.modules.pay.entity.PayRecordEntity;
import io.grx.modules.tx.entity.TxExtensionEntity;
import io.grx.modules.tx.enums.ExtensionStatus;
import io.grx.modules.tx.service.TxExtensionService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.grx.common.utils.CharUtils;
import io.grx.common.utils.Query;
import io.grx.modules.tx.dao.TxUserDao;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.enums.TxStatus;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxMessageService;
import io.grx.modules.tx.service.TxUserBalanceService;

@Component("txTask")
public class TxTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private TxUserBalanceService txUserBalanceService;

    @Autowired
    private TxMessageService txMessageService;

    @Autowired
    private TxUserDao txUserDao;

    @Autowired
    private PayRecordDao payRecordDao;

    @Autowired
    private TxExtensionService txExtensionService;

    public void deleteExpiredTx(){
        logger.info("Start to delete expired pending reports.");
        List<TxBaseEntity> expiredPendingTransactions = txBaseService.getExpiredPendingList();
        if (CollectionUtils.isEmpty(expiredPendingTransactions)) {
            logger.info("No expired pending transactions");
            return;
        }

        for (TxBaseEntity tx : expiredPendingTransactions) {
            try {
                txBaseService.delete(tx.getTxId());

                try {
                    if (tx.getBorrowerUserId() != null && (tx.getStatus() == TxStatus.NEW || tx.getStatus() ==
                            TxStatus.REJECTED)
                            && tx.getFeeAmount().doubleValue() > 0) {
                        txUserBalanceService.addBalanceByDeletingTx(tx);
                    }
                } catch (Exception e) {
                    logger.error("[IMPORTANT] failed add user balance");
                }


                txMessageService.sendMsgForExpiredTransaction(tx);
            } catch (Exception e) {
                logger.error("Failed to delete expired transaction", e);
            }
        }
    }

    @Transactional
    public void markOverdueStatus(){
        logger.info("Start to mark overdue tx.");
        txBaseService.updateOverdueTx(null);
        logger.info("End mark overdue tx.");
    }

    public void patchTxUserNames() {
        logger.info("Start to patch tx user name pinyin.");
        int page = 1;

        boolean hasMoreRecords = true;
        while (hasMoreRecords) {
            hasMoreRecords = patchTxUserNameByPage(page++);
        }
        logger.info("End patch tx user name pinyin.");
    }

    @Transactional
    public boolean patchTxUserNameByPage(int page) {
        Query q = new Query(new HashMap<>());
        q.setPage(page);
        q.setLimit(100);

        List<TxUserEntity> pendingList = txUserDao.queryNonEmptyNameList(q);
        for (TxUserEntity userEntity : pendingList) {
            if (StringUtils.isBlank(userEntity.getNamePinyin())) {
                userEntity.setNamePinyin(StringUtils.join(CharUtils.getPinyin(userEntity.getName()), ","));
                txUserDao.updateNamePinyin(userEntity);
            }
        }

        logger.info("Patched {} user name pinyin", pendingList.size());
        return pendingList.size() >= q.getLimit();
    }

    public void patchExtensions(String startDate) {
        Date startTime = null;

        if (StringUtils.isNotBlank(startDate)) {
            startTime = DateUtils.parseDate(startDate);
        }

        List<PayRecordEntity> payRecordEntities = payRecordDao.queryByPaidExtensions(startTime);
        logger.info("Totally {} pay records to handle", payRecordEntities.size());

        for (PayRecordEntity payRecordEntity : payRecordEntities) {
            TxUserEntity payUser = txUserDao.queryObject(payRecordEntity.getPayUserId());
            if (payRecordEntity.getExtensionId() != null) {
                TxExtensionEntity txExtensionEntity = txExtensionService.queryObject(payRecordEntity
                        .getExtensionId());

                try {
                    txExtensionService.updateStatus(txExtensionEntity, ExtensionStatus.CONFIRMED, payUser);

                    txMessageService.sendMsgForConfirmingExtension(txExtensionEntity);
                } catch (Throwable t) {
                    logger.error("failed to confirm extension", t);
                }

            }
            logger.info("Process pay record: ", payRecordEntity.getTrxId());
        }
    }
}
