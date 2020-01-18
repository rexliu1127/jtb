package io.grx.modules.tx.service.impl;

import io.grx.modules.sys.service.SysConfigService;
import io.grx.modules.tx.dto.TxUserRewardStatVO;
import io.grx.modules.tx.dto.TxUserRewardSum;
import io.grx.modules.tx.dto.TxUserRewardVO;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxExtensionEntity;
import io.grx.modules.tx.entity.TxUserInviteEntity;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxUserBalanceService;
import io.grx.modules.tx.service.TxUserInviteService;
import io.grx.modules.tx.utils.TxConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.grx.modules.tx.dao.TxUserRewardDao;
import io.grx.modules.tx.entity.TxUserRewardEntity;
import io.grx.modules.tx.service.TxUserRewardService;
import org.springframework.transaction.annotation.Transactional;


@Service("txUserRewardService")
public class TxUserRewardServiceImpl implements TxUserRewardService {
	@Autowired
	private TxUserRewardDao txUserRewardDao;

	@Autowired
    private TxUserInviteService txUserInviteService;

	@Autowired
    private SysConfigService sysConfigService;

	@Autowired
    private TxUserBalanceService txUserBalanceService;

	@Autowired
    private TxBaseService txBaseService;
	
	@Override
	public TxUserRewardEntity queryObject(Long id){
		return txUserRewardDao.queryObject(id);
	}
	
	@Override
	public List<TxUserRewardEntity> queryList(Map<String, Object> map){
		return txUserRewardDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return txUserRewardDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(TxUserRewardEntity txUserReward){
		txUserRewardDao.save(txUserReward);
	}
	
	@Override
	@Transactional
	public void update(TxUserRewardEntity txUserReward){
		txUserRewardDao.update(txUserReward);
	}
	
	@Override
	public void delete(Long id){
		txUserRewardDao.delete(id);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		txUserRewardDao.deleteBatch(ids);
	}

    @Override
    @Transactional
    public void addUserReward(TxBaseEntity txBaseEntity) {
	    if (txBaseEntity.getFeeAmount().doubleValue() == 0) {
	        return;
        }

        TxUserInviteEntity userInviteEntity = txUserInviteService.queryByUseId(txBaseEntity.getBorrowerUserId());
        if (userInviteEntity != null) {
            BigDecimal firstCommissionRate = get1stCommissionRate();
            if (firstCommissionRate.doubleValue() > 0) {
                BigDecimal reward = txBaseEntity.getFeeAmount().multiply(firstCommissionRate)
                        .setScale(2, RoundingMode.HALF_UP);
                if (reward.doubleValue() > 0) {
                    TxUserRewardEntity userRewardEntity = new TxUserRewardEntity();

                    userRewardEntity.setUserId(userInviteEntity.getInviterUserId());
                    userRewardEntity.setTxId(txBaseEntity.getTxId());
                    userRewardEntity.setReward(reward);
                    userRewardEntity.setLevel(1);
                    userRewardEntity.setInviteeUserId(txBaseEntity.getBorrowerUserId());
                    userRewardEntity.setCreateTime(new Date());

                    save(userRewardEntity);

                    txUserBalanceService.addBalanceByReward(userRewardEntity);
                }
            }

            userInviteEntity = txUserInviteService.queryByUseId(userInviteEntity.getInviterUserId());
            if (userInviteEntity != null) {
                BigDecimal secondCommissionRate = get2ndCommissionRate();
                if (secondCommissionRate.doubleValue() > 0) {
                    BigDecimal reward = txBaseEntity.getFeeAmount().multiply(secondCommissionRate)
                            .setScale(2, RoundingMode.HALF_UP);
                    if (reward.doubleValue() > 0) {
                        TxUserRewardEntity userRewardEntity = new TxUserRewardEntity();

                        userRewardEntity.setUserId(userInviteEntity.getInviterUserId());
                        userRewardEntity.setInviteeUserId(txBaseEntity.getBorrowerUserId());
                        userRewardEntity.setTxId(txBaseEntity.getTxId());
                        userRewardEntity.setReward(reward);
                        userRewardEntity.setLevel(2);
                        userRewardEntity.setCreateTime(new Date());

                        save(userRewardEntity);

                        txUserBalanceService.addBalanceByReward(userRewardEntity);
                    }
                }
            }
        }
    }

    @Override
    public void addUserReward(TxExtensionEntity txExtensionEntity) {
        if (txExtensionEntity.getFeeAmount().doubleValue() == 0) {
            return;
        }

        TxBaseEntity txBaseEntity = txBaseService.queryObjectNoAcl(txExtensionEntity.getTxId());
        TxUserInviteEntity userInviteEntity = txUserInviteService.queryByUseId(txBaseEntity.getBorrowerUserId());
        if (userInviteEntity != null) {
            BigDecimal firstCommissionRate = get1stCommissionRate();
            if (firstCommissionRate.doubleValue() > 0) {
                BigDecimal reward = txExtensionEntity.getFeeAmount().multiply(firstCommissionRate)
                        .setScale(2, RoundingMode.HALF_UP);
                if (reward.doubleValue() > 0) {
                    TxUserRewardEntity userRewardEntity = new TxUserRewardEntity();

                    userRewardEntity.setUserId(userInviteEntity.getInviterUserId());
                    userRewardEntity.setExtensionId(txExtensionEntity.getExtensionId());
                    userRewardEntity.setReward(reward);
                    userRewardEntity.setLevel(1);
                    userInviteEntity.setCreateTime(new Date());

                    save(userRewardEntity);

                    txUserBalanceService.addBalanceByReward(userRewardEntity);
                }
            }

            userInviteEntity = txUserInviteService.queryByUseId(userInviteEntity.getInviterUserId());
            if (userInviteEntity != null) {
                BigDecimal secondCommissionRate = get2ndCommissionRate();
                if (secondCommissionRate.doubleValue() > 0) {
                    BigDecimal reward = txExtensionEntity.getFeeAmount().multiply(secondCommissionRate)
                            .setScale(2, RoundingMode.HALF_UP);
                    if (reward.doubleValue() > 0) {
                        TxUserRewardEntity userRewardEntity = new TxUserRewardEntity();

                        userRewardEntity.setUserId(userInviteEntity.getInviterUserId());
                        userRewardEntity.setExtensionId(txExtensionEntity.getExtensionId());
                        userRewardEntity.setReward(reward);
                        userRewardEntity.setLevel(2);
                        userInviteEntity.setCreateTime(new Date());

                        save(userRewardEntity);

                        txUserBalanceService.addBalanceByReward(userRewardEntity);
                    }
                }
            }
        }
    }

    @Override
    public List<TxUserRewardVO> queryListByUser(Map<String, Object> params) {
        return txUserRewardDao.queryListByUser(params);
    }

    @Override
    public double sumUserReward(Long userId) {
        return txUserRewardDao.sumUserReward(userId);
    }

    @Override
    public List<TxUserRewardStatVO> queryRewardStat(Map<String, Object> params) {
        return txUserRewardDao.queryRewardStat(params);
    }

    @Override
    public int queryRewardStatTotal(Map<String, Object> params) {
        return txUserRewardDao.queryRewardStatTotal(params);
    }

    @Override
    public TxUserRewardSum queryRewardSum() {
        return txUserRewardDao.queryRewardSum();
    }

    private BigDecimal get1stCommissionRate() {
        String feeStr = sysConfigService.getValue(TxConstants.COMMISSION_RATE_1);
        if (StringUtils.isBlank(feeStr)) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(feeStr);
        }
    }

    private BigDecimal get2ndCommissionRate() {
        String feeStr = sysConfigService.getValue(TxConstants.COMMISSION_RATE_2);
        if (StringUtils.isBlank(feeStr)) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(feeStr);
        }
    }
}
