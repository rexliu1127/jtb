package io.grx.wx.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.grx.common.utils.DateUtils;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.enums.TxStatus;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.service.TxUserService;
import io.grx.wx.dto.TxDto;

@Component
public class TxDtoConverter {
    @Autowired
    private TxUserService txUserService;

    private static final String DEFAULT_HEAD_IMG = "../img/accoubt/photo.svg";

    public List<TxDto> convert(List<TxBaseEntity> baseEntities) {
        return convert(baseEntities, false);
    }

    public List<TxDto> convert(List<TxBaseEntity> baseEntities, boolean isForCredit) {
        List<TxDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(baseEntities)) {
            for (TxBaseEntity entity : baseEntities) {
                result.add(convert(entity, isForCredit));
            }
        }
        return result;
    }

    private TxDto convert(TxBaseEntity entity, boolean isForCredit) {
        TxDto dto = new TxDto();
        dto.setTxId(entity.getTxId());
        dto.setAmount(entity.getAmount());
        dto.setRate(entity.getRate());
        dto.setBeginDate(entity.getBeginDate());
        dto.setEndDate(entity.getEndDate());
        if (!isForCredit) {
            dto.setBorrowerName(entity.getBorrowerName());
            dto.setLenderName(entity.getLenderName());

            if (entity.getBorrowerUserId() != null) {
                TxUserEntity borrower = txUserService.queryObject(entity.getBorrowerUserId());
                dto.setBorrowerHeadImg(borrower.getHeadImgUrl());
            } else {
                dto.setBorrowerHeadImg(DEFAULT_HEAD_IMG);
            }

            if (entity.getLenderUserId() != null) {
                TxUserEntity lender = txUserService.queryObject(entity.getLenderUserId());
                dto.setLenderHeadImg(lender.getHeadImgUrl());
            } else {
                dto.setLenderHeadImg(DEFAULT_HEAD_IMG);
            }

            dto.setDaysBeforeInvalid(7 - DateUtils.daysBetween(entity.getCreateTime(), new Date()));

            dto.setUseDay(DateUtils.daysBetween(entity.getBeginDate(), entity.getEndDate()));

            dto.setCreatedByBorrower(entity.getCreateUserId().equals(entity.getBorrowerUserId()));

            dto.setCreatedByLender(entity.getCreateUserId().equals(entity.getLenderUserId()));
        }

        dto.setTxStatus(entity.getStatus());


        if (entity.getStatus() == TxStatus.COMPLETED) {
            dto.setRepaymentDate(entity.getRepayDate());
        }

        dto.setStatusLabel(entity.getStatus().getDisplayName());

        return dto;
    }
}
