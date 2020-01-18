package io.grx.modules.sys.service.impl;

import io.grx.modules.sys.dao.SysLogDao;
import io.grx.modules.sys.entity.SysLogEntity;
import io.grx.modules.sys.service.SysLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service("sysLogService")
public class SysLogServiceImpl implements SysLogService {
	@Autowired
	private SysLogDao sysLogDao;
	
	@Override
	public SysLogEntity queryObject(Long id){
		return sysLogDao.queryObject(id);
	}
	
	@Override
	public List<SysLogEntity> queryList(Map<String, Object> map){
		return sysLogDao.queryList(map);
	}

	@Override
	public List<SysLogEntity> queryListByMerchantNo(Map<String, Object> map){
		return sysLogDao.queryListByMerchantNo(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysLogDao.queryTotal(map);
	}

	@Override
	public int queryTotalByMerchantNo(Map<String, Object> map){
		return sysLogDao.queryTotalByMerchantNo(map);
	}
	
	@Override
	public void save(SysLogEntity sysLog){
		sysLogDao.save(sysLog);
	}
	
	@Override
	public void delete(Long id){
		sysLogDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		sysLogDao.deleteBatch(ids);
	}
	
}
