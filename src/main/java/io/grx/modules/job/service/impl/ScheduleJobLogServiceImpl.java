package io.grx.modules.job.service.impl;

import io.grx.modules.job.service.ScheduleJobLogService;
import io.grx.modules.job.dao.ScheduleJobLogDao;
import io.grx.modules.job.entity.ScheduleJobLogEntity;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl implements ScheduleJobLogService {
	@Autowired
	private ScheduleJobLogDao scheduleJobLogDao;
	
	@Override
	public ScheduleJobLogEntity queryObject(Long jobId) {
		return scheduleJobLogDao.queryObject(jobId);
	}

	@Override
	public List<ScheduleJobLogEntity> queryList(Map<String, Object> map) {
		return scheduleJobLogDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return scheduleJobLogDao.queryTotal(map);
	}

	@Override
	public void save(ScheduleJobLogEntity log) {
		scheduleJobLogDao.save(log);
	}

}
