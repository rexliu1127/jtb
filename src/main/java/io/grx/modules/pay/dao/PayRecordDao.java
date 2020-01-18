package io.grx.modules.pay.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.grx.modules.pay.entity.PayRecordEntity;
import io.grx.modules.sys.dao.BaseDao;

/**
 * 支付记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-08 01:07:58
 */
@Mapper
public interface PayRecordDao extends BaseDao<PayRecordEntity> {

    PayRecordEntity queryByTrxId(String trxId);

    long sumPaidAmount(@Param(value = "startTime") Date startTme, @Param(value = "endTime") Date endTime);

    List<PayRecordEntity> queryByPaidExtensions(@Param(value = "startTime") Date startTme);
}
