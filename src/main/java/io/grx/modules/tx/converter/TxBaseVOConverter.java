package io.grx.modules.tx.converter;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.sql.visitor.functions.Char;
import io.grx.common.utils.CharUtils;
import io.grx.common.utils.ShiroUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.grx.common.utils.DateUtils;
import io.grx.modules.tx.dto.TxBaseVO;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxExtensionEntity;
import io.grx.modules.tx.entity.TxRepaymentEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.enums.RepaymentStatus;
import io.grx.modules.tx.service.TxExtensionService;
import io.grx.modules.tx.service.TxRepaymentService;
import io.grx.modules.tx.service.TxUserService;

@Service
public class TxBaseVOConverter {
    @Autowired
    private TxRepaymentService repaymentService;

    @Autowired
    private TxExtensionService extensionService;

    @Autowired
    private TxUserService userService;

    public List<TxBaseVO> convert(List<TxBaseEntity> baseEntities) {
        List<TxBaseVO> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(baseEntities)) {
            for (TxBaseEntity entity : baseEntities) {
                result.add(convert(entity));
            }
        }
        return result;
    }

    private TxBaseVO convert(TxBaseEntity entity) {
        TxBaseVO dto = new TxBaseVO();
        dto.setTxId(entity.getTxId());
        dto.setAmount(entity.getAmount());
        if (entity.getOutstandingAmount() != null) {
            dto.setOutstandingAmount(entity.getOutstandingAmount());
        }
        dto.setInterest(entity.getInterest().doubleValue());
        dto.setRate(entity.getRate());
        dto.setBeginDate(DateUtils.formateDate(entity.getBeginDate()));
        dto.setEndDate(DateUtils.formateDate(entity.getEndDate()));
        if (ShiroUtils.isSuperAdmin()) {
            dto.setBorrowerName(CharUtils.maskMiddleChars(entity.getBorrowerName(), 0, 1));
            dto.setLenderName(CharUtils.maskMiddleChars(entity.getLenderName(), 0, 1));
        } else {
            dto.setBorrowerName(entity.getBorrowerName());
            dto.setLenderName(entity.getLenderName());
        }
        dto.setStatus(entity.getStatus());

        //交易全局变量
        dto.setTxUuid(entity.getTxUuid());

        if (entity.getBorrowerUserId() != null) {
            TxUserEntity borrower = userService.queryObject(entity.getBorrowerUserId());
            if (borrower != null) {
                if (ShiroUtils.isSuperAdmin()) {
                    dto.setBorrowerIdNo(CharUtils.maskMiddleChars(StringUtils.defaultString(borrower.getIdNo()), 4, 4));
                    dto.setBorrowerMobile(CharUtils.maskMiddleChars(StringUtils.defaultString(borrower.getMobile()), 3, 2));
                } else {
                    dto.setBorrowerIdNo(StringUtils.defaultString(borrower.getIdNo()));
                    dto.setBorrowerMobile(StringUtils.defaultString(borrower.getMobile()));
                }
            }
        }

        if (entity.getLenderUserId() != null) {
            TxUserEntity lender = userService.queryObject(entity.getLenderUserId());
            if (lender != null) {
                if (ShiroUtils.isSuperAdmin()) {
                    dto.setLenderIdNo(CharUtils.maskMiddleChars(StringUtils.defaultString(lender.getIdNo()), 4, 4));
                    dto.setLenderMobile(CharUtils.maskMiddleChars(lender.getMobile(), 3, 2));
                } else {
                    dto.setLenderIdNo(StringUtils.defaultString(lender.getIdNo()));
                    dto.setLenderMobile(lender.getMobile());
                }
            }
        }

        if (entity.getRepayDate() != null) {
            dto.setRepayDate(DateUtils.formateDate(entity.getRepayDate()));
        }

//        TxRepaymentEntity repaymentEntity = repaymentService.getLastRepaymentByTx(entity.getTxId());
//        if (repaymentEntity != null && repaymentEntity.getStatus() == RepaymentStatus.CONFIRMED) {
//            dto.setActualEndDate(DateUtils.formateDate(entity.getUpdateTime()));
//        }

        List<TxExtensionEntity> extensionEntityList = extensionService.getExtensionsByTx(entity.getTxId());
        dto.setExtensionCount(extensionEntityList.size());

        return dto;
    }
}
