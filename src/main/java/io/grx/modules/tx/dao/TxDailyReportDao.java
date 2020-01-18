package io.grx.modules.tx.dao;

import org.apache.ibatis.annotations.Mapper;

import io.grx.modules.sys.dao.BaseDao;
import io.grx.modules.tx.dto.TxStatSum;
import io.grx.modules.tx.entity.TxDailyReportEntity;

/**
 * 每日借条统计
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-01-27 22:50:18
 */
@Mapper
public interface TxDailyReportDao extends BaseDao<TxDailyReportEntity> {
	TxStatSum queryStatSum();

	void generateLastDaySum();
}
