package io.grx.wx.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.grx.common.utils.DateUtils;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxRepaymentEntity;
import io.grx.modules.tx.enums.RepaymentStatus;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.wx.dto.TxRepaymentDto;

@Component
public class TxRepaymentDtoConverter {
    @Autowired
    private TxBaseService txBaseService;

    public List<TxRepaymentDto> convert(List<TxRepaymentEntity> repaymentEntities) {
        List<TxRepaymentDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(repaymentEntities)) {
            for (TxRepaymentEntity entity : repaymentEntities) {
                result.add(convert(entity));
            }
        }
        return result;
    }

    private TxRepaymentDto convert(TxRepaymentEntity entity) {
        TxRepaymentDto dto = new TxRepaymentDto();
        dto.setRepaymentId(entity.getRepaymentId());
        dto.setTxId(entity.getTxId());

        TxBaseEntity baseEntity = txBaseService.queryObject(entity.getTxId());

        dto.setAmount(baseEntity.getAmount());
        dto.setInterest(baseEntity.getInterest().doubleValue());
        dto.setOutstandingAmount(baseEntity.getOutstandingAmount());
        dto.setOutstandingInterest(baseEntity.getOutstandingInterest().doubleValue());
        dto.setBorrowerName(baseEntity.getBorrowerName());
        dto.setLenderName(baseEntity.getLenderName());

        dto.setBeginDate(DateUtils.formateDate(baseEntity.getBeginDate()));
        dto.setEndDate(DateUtils.formateDate(baseEntity.getEndDate()));

        if (entity.getStatus() == RepaymentStatus.CONFIRMED) {
            dto.setActualEndDate(DateUtils.formateDate(entity.getUpdateTime()));
        } else {
            dto.setActualEndDate("");
        }

        dto.setRepaymentStatus(entity.getStatus().getValue());
        dto.setRepaymentStatusLabel(entity.getStatus().getDisplayName());
        return dto;
    }
}
