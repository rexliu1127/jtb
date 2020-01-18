package io.grx.modules.tx.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.grx.common.utils.DateUtils;
import io.grx.modules.tx.dto.TxComplainVo;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxComplainEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxUserService;

@Service
public class TxComplainVOConverter {
    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private TxUserService txUserService;

    public List<TxComplainVo> convert(List<TxComplainEntity> baseEntities) {
        List<TxComplainVo> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(baseEntities)) {
            for (TxComplainEntity entity : baseEntities) {
                result.add(convert(entity));
            }
        }
        return result;
    }

    private TxComplainVo convert(TxComplainEntity entity) {
        TxComplainVo dto = new TxComplainVo();
        dto.setComplainId(entity.getComplainId());
        dto.setTxId(entity.getTxId());

        TxBaseEntity txBaseEntity = txBaseService.queryObjectNoAcl(entity.getTxId());
        if (txBaseEntity != null) {
            dto.setAmount(txBaseEntity.getAmount());
            dto.setTxUuid(txBaseEntity.getTxUuid());
            dto.setBeginDate(DateUtils.formateDate(txBaseEntity.getBeginDate()));
            dto.setEndDate(DateUtils.formateDate(txBaseEntity.getEndDate()));
            dto.setTxStatusLabel(txBaseEntity.getStatus().getDisplayName());
            TxUserEntity borrower = txUserService.queryObject(txBaseEntity.getBorrowerUserId());
            if (borrower != null) {
                dto.setBorrowerIdNo(borrower.getIdNo());
                dto.setBorrowerMobile(borrower.getMobile());
                dto.setBorrowerName(borrower.getName());
                dto.setBorrowerUserId(borrower.getUserId());
            }

            TxUserEntity lender = txUserService.queryObject(txBaseEntity.getLenderUserId());
            if (borrower != null) {
                dto.setLenderIdNo(lender.getIdNo());
                dto.setLenderMobile(lender.getMobile());
                dto.setLenderName(lender.getName());
                dto.setLenderUserId(lender.getUserId());
            }
        }

        dto.setComplainTypeDesc(entity.getComplainType().getDisplayName());

        dto.setRemark(entity.getRemark());
        dto.setCreateTime(entity.getCreateTime());
        dto.setProcessorComment(entity.getProcessorComment());

        if (entity.getStatus() != null) {
            dto.setComplainResult(entity.getStatus().getValue());
            dto.setComplainResultDesc(entity.getStatus().getDisplayName());
        }

        if (StringUtils.isNotBlank(entity.getImagePath())) {
            dto.setImagePaths(StringUtils.split(entity.getImagePath(), ","));
        }

        return dto;
    }
}
