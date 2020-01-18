package io.grx.modules.sys.service.impl;

import io.grx.modules.sys.entity.SysFunEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.grx.modules.sys.dao.SysFunTypeDao;
import io.grx.modules.sys.entity.SysFunTypeEntity;
import io.grx.modules.sys.service.SysFunTypeService;



@Service("sysFunTypeService")
public class SysFunTypeServiceImpl implements SysFunTypeService {
	@Autowired
	private SysFunTypeDao sysFunTypeDao;
	
	@Override
	public SysFunTypeEntity queryObject(Long id){
		return sysFunTypeDao.queryObject(id);
	}
	
	@Override
	public List<SysFunTypeEntity> queryList(Map<String, Object> map){
		return sysFunTypeDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysFunTypeDao.queryTotal(map);
	}
	
	@Override
	public void save(SysFunTypeEntity sysFunType){
		sysFunTypeDao.save(sysFunType);
	}
	
	@Override
	public void update(SysFunTypeEntity sysFunType){
		sysFunTypeDao.update(sysFunType);
	}
	
	@Override
	public void delete(Long id){
		sysFunTypeDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		sysFunTypeDao.deleteBatch(ids);
	}

	@Override
	public int queryCountByConditions(Map<String, Object> map){ return sysFunTypeDao.queryCountByConditions(map); }

	@Override
	public SysFunTypeEntity queryObjectByConditions(Map<String, Object> map){ return sysFunTypeDao.queryObjectByConditions(map); }

	@Override
	public void saveFunTypeList(List<SysFunTypeEntity> sysFunTypeList){
		sysFunTypeDao.saveFunTypeList(sysFunTypeList);
	}

	@Override
	public List<SysFunTypeEntity>  queryInfo(String merchant_no){
		return sysFunTypeDao.queryInfo(merchant_no);
	}
	
}
