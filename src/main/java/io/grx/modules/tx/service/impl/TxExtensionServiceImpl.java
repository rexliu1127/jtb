package io.grx.modules.tx.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.grx.modules.tx.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.common.exception.RRException;
import io.grx.modules.sys.service.SysConfigService;
import io.grx.modules.tx.dao.TxExtensionDao;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxExtensionEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.enums.ExtensionStatus;
import io.grx.modules.tx.utils.TxConstants;


@Service("txExtensionService")
public class TxExtensionServiceImpl implements TxExtensionService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TxExtensionDao txExtensionDao;

    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private TxOverdueRecordService txOverdueRecordService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private TxRepayPlanService txRepayPlanService;

    @Autowired
    private TxUserRewardService txUserRewardService;

    @Override
    public TxExtensionEntity queryObject(Long extensionId) {
        return txExtensionDao.queryObject(extensionId);
    }

    @Override
    public List<TxExtensionEntity> queryList(Map<String, Object> map) {
        return txExtensionDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return txExtensionDao.queryTotal(map);
    }

    @Override
    @Transactional
    public void save(TxExtensionEntity txExtension) {
        txExtensionDao.save(txExtension);
    }

    @Override
    @Transactional
    public void update(TxExtensionEntity txExtension) {
        txExtensionDao.update(txExtension);
    }

    @Override
    @Transactional
    public void delete(Long extensionId) {
        txExtensionDao.delete(extensionId);
    }

    @Override
    @Transactional
    public void deleteBatch(Long[] extensionIds) {
        txExtensionDao.deleteBatch(extensionIds);
    }

    /**
     * 取出最新的展期
     *
     * @param txId
     * @return
     */
    @Override
    public TxExtensionEntity getLastExtensionByTx(final long txId) {
        return txExtensionDao.getLastExtensionByTx(txId);
    }

    /**
     * 取出所有展期
     *
     * @param txId
     * @return
     */
    @Override
    public List<TxExtensionEntity> getExtensionsByTx(final long txId) {
        return txExtensionDao.getExtensionsByTx(txId);
    }

    /**
     * 修改展期状态
     *
     * @param status
     */
    @Override
    @Transactional
    public void updateStatus(TxExtensionEntity extensionEntity, final ExtensionStatus status, TxUserEntity userEntity) {
        TxBaseEntity baseEntity = txBaseService.queryObjectNoAcl(extensionEntity.getTxId());

        if (extensionEntity.getStatus() != ExtensionStatus.NEW) {
            throw new RRException("该展期已经被确认或拒绝", 401);
        }

        if (((status == ExtensionStatus.CONFIRMED || status == ExtensionStatus.REJECTED) && !userEntity.getUserId()
                .equals
                        (baseEntity.getBorrowerUserId()))
                || (status == ExtensionStatus.CANCELLED && !userEntity.getUserId().equals(baseEntity.getLenderUserId
                ()))) {
            throw new RRException("Invalid request", 401);
        }
        extensionEntity.setStatus(status);
        extensionEntity.setUpdateTime(new Date());

        if (status == ExtensionStatus.CONFIRMED) {
            txBaseService.extendTx(baseEntity, extensionEntity.getExtendAmount(), extensionEntity.getNewEndDate(),
                    extensionEntity.getRate());

            txRepayPlanService.createRepayPlanByExtension(baseEntity, extensionEntity.getCreateTime());

            txUserRewardService.addUserReward(extensionEntity);
        }

        update(extensionEntity);

        if (status == ExtensionStatus.CANCELLED || status == ExtensionStatus.REJECTED) {
            txBaseService.updateOverdueTx(baseEntity.getTxId());
        }
    }

    @Override
    public BigDecimal getExtensionFee() {
        String feeStr = sysConfigService.getValue(TxConstants.EXTENSION_FEE);
        if (StringUtils.isBlank(feeStr)) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(feeStr);
        }
    }

    @Override
    public boolean isFreeExtensionLenderMobile(String mobile) {
        List<String> freeExLenderMobiles = sysConfigService.getConfigObject(
                TxConstants.FREE_EX_LENDER_MOBILE, ArrayList.class);

        return freeExLenderMobiles.contains(mobile);
    }
}
