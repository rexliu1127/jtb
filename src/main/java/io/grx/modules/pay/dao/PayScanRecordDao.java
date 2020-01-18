package io.grx.modules.pay.dao;

import io.grx.modules.pay.entity.PayScanRecordEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付扫描记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-27 16:07:28
 */
@Mapper
public interface PayScanRecordDao extends BaseDao<PayScanRecordEntity> {
	
}
