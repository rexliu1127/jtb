package io.grx.modules.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.grx.modules.sys.dao.SysFunDao;
import io.grx.modules.sys.entity.SysFunEntity;
import io.grx.modules.sys.service.SysFunService;



@Service("sysFunService")
public class SysFunServiceImpl implements SysFunService {
	@Autowired
	private SysFunDao sysFunDao;
	
	@Override
	public SysFunEntity queryObject(Long id){
		return sysFunDao.queryObject(id);
	}
	
	@Override
	public List<SysFunEntity> queryList(Map<String, Object> map){
		return sysFunDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysFunDao.queryTotal(map);
	}
	
	@Override
	public void save(SysFunEntity sysFun){
		sysFunDao.save(sysFun);
	}
	
	@Override
	public void update(SysFunEntity sysFun){
		sysFunDao.update(sysFun);
	}
	
	@Override
	public void delete(Long id){
		sysFunDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		sysFunDao.deleteBatch(ids);
	}

	@Override
	public void updateRistBatch(Long[] ids){
		sysFunDao.updateRistBatch(ids);
	}

	@Override
	public SysFunEntity queryinfo(String merchant_no){
		return sysFunDao.queryinfo(merchant_no);
	}
	
}
