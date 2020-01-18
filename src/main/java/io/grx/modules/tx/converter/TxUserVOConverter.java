package io.grx.modules.tx.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.grx.common.utils.CharUtils;
import io.grx.common.utils.ShiroUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.grx.modules.tx.dto.TxUserVO;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.wx.dto.TxBorrowerSummary;

@Service
public class TxUserVOConverter {
    @Autowired
    private TxBaseService txBaseService;

    public List<TxUserVO> convert(List<TxUserEntity> baseEntities) {
        List<TxUserVO> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(baseEntities)) {
            for (TxUserEntity entity : baseEntities) {
                result.add(convert(entity));
            }
        }
        return result;
    }

    private TxUserVO convert(TxUserEntity entity) {
        TxUserVO dto = new TxUserVO();

        dto.setUserId(entity.getUserId());
        if (ShiroUtils.isSuperAdmin()) {
            dto.setName(CharUtils.maskMiddleChars(entity.getName(), 0, 1));
            dto.setMobile(CharUtils.maskMiddleChars(entity.getMobile(), 3, 2));
            dto.setIdNo(CharUtils.maskMiddleChars(entity.getIdNo(), 4, 4));
        } else {
            dto.setName(entity.getName());
            dto.setMobile(entity.getMobile());
            dto.setIdNo(entity.getIdNo());
        }

        TxBorrowerSummary borrowerSummary = txBaseService.getBorrowerSummaryByUserId(entity.getUserId());
        dto.setTotalBorrowedAmount(borrowerSummary.getTotalBorrowedAmount());
        dto.setTotalBorrowedCount(borrowerSummary.getTotalBorrowedCount());
        dto.setTotalRepayAmount(new BigDecimal(borrowerSummary.getTotalRepaidAmount()));
        dto.setCurrentOverdueAmount(borrowerSummary.getCurrentOverdueAmount());
        dto.setCurrentOverdueCount(borrowerSummary.getCurrentOverdueCount());
        dto.setLenderCount((long) borrowerSummary.getTotalLenderCount());

        return dto;
    }
}
