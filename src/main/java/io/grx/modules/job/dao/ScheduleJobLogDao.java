package io.grx.modules.job.dao;

import io.grx.modules.sys.dao.BaseDao;
import io.grx.modules.job.entity.ScheduleJobLogEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月1日 下午10:30:02
 */
@Mapper
public interface ScheduleJobLogDao extends BaseDao<ScheduleJobLogEntity> {
	
}
