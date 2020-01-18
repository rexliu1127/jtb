package io.grx.modules.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.grx.modules.sys.dao.SysNoticeDao;
import io.grx.modules.sys.entity.SysNoticeEntity;
import io.grx.modules.sys.service.SysNoticeService;



@Service("sysNoticeService")
public class SysNoticeServiceImpl implements SysNoticeService {
	@Autowired
	private SysNoticeDao sysNoticeDao;
	
	@Override
	public SysNoticeEntity queryObject(Long noticeId){
		return sysNoticeDao.queryObject(noticeId);
	}
	
	@Override
	public List<SysNoticeEntity> queryList(Map<String, Object> map){
		return sysNoticeDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysNoticeDao.queryTotal(map);
	}
	
	@Override
	public void save(SysNoticeEntity sysNotice){
		sysNoticeDao.save(sysNotice);
	}
	
	@Override
	public void update(SysNoticeEntity sysNotice){
		sysNoticeDao.update(sysNotice);
	}
	
	@Override
	public void delete(Long noticeId){
		sysNoticeDao.delete(noticeId);
	}
	
	@Override
	public void deleteBatch(Long[] noticeIds){
		sysNoticeDao.deleteBatch(noticeIds);
	}
	
}
