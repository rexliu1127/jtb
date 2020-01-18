package io.grx.wx.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.grx.common.utils.DateUtils;
import io.grx.wx.dto.TxRepayDto;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxExtensionEntity;
import io.grx.modules.tx.entity.TxRepaymentEntity;
import io.grx.modules.tx.service.TxExtensionService;
import io.grx.modules.tx.service.TxRepaymentService;

@Service
public class TxRepayDtoConverter {
    @Autowired
    private TxRepaymentService repaymentService;

    @Autowired
    private TxExtensionService extensionService;

    public List<TxRepayDto> convert(List<TxBaseEntity> baseEntities) {
        List<TxRepayDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(baseEntities)) {
            for (TxBaseEntity entity : baseEntities) {
                result.add(convert(entity));
            }
        }
        return result;
    }

    public TxRepayDto convert(TxBaseEntity entity) {
        TxRepayDto dto = new TxRepayDto();
        dto.setTxId(entity.getTxId());
        dto.setAmount(entity.getAmount());
        dto.setInterest(entity.getInterest().doubleValue());
        dto.setOutstandingAmount(entity.getOutstandingAmount() != null ? entity.getOutstandingAmount() : 0);
        dto.setOutstandingInterest(entity.getOutstandingInterest() != null
                ? entity.getOutstandingInterest().doubleValue() : 0.0);
        dto.setRate(entity.getRate());
        dto.setBeginDate(DateUtils.formateDate(entity.getBeginDate()));
        dto.setEndDate(DateUtils.formateDate(entity.getEndDate()));
        dto.setBorrowerName(entity.getBorrowerName());
        dto.setLenderName(entity.getLenderName());
        dto.setUsageType(entity.getUsageType().getValue());
        dto.setUsageTypeLabel(entity.getUsageType().getDisplayName());
        dto.setTxStatus(entity.getStatus().getValue());
        dto.setTxStatusLabel(entity.getStatus().getDisplayName());

        TxExtensionEntity extensionEntity = extensionService.getLastExtensionByTx(entity.getTxId());

        TxRepaymentEntity repaymentEntity = repaymentService.getLastRepaymentByTx(entity.getTxId());

        if (extensionEntity != null && (repaymentEntity == null || extensionEntity.getCreateTime().getTime() >
                repaymentEntity.getCreateTime().getTime())) {
            dto.setExtensionStatus(extensionEntity.getStatus().getValue());
            dto.setExtensionStatusLabel("展期" + extensionEntity.getStatus().getDisplayName());
        }

        if (repaymentEntity != null && (extensionEntity == null || repaymentEntity.getCreateTime().getTime() >
                extensionEntity.getCreateTime().getTime())) {
            dto.setRepaymentStatus(repaymentEntity.getStatus().getValue());
            dto.setRepaymentStatusLabel("还款" + repaymentEntity.getStatus().getDisplayName());
        }

        dto.setDaysToPay((int) DateUtils.daysBetween(new Date(), entity.getEndDate()));
        return dto;
    }
}
