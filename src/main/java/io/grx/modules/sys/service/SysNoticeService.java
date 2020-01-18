package io.grx.modules.sys.service;

import io.grx.modules.sys.entity.SysNoticeEntity;

import java.util.List;
import java.util.Map;

/**
 * 公告表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-20 10:13:19
 */
public interface SysNoticeService {
	
	SysNoticeEntity queryObject(Long noticeId);
	
	List<SysNoticeEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysNoticeEntity sysNotice);
	
	void update(SysNoticeEntity sysNotice);
	
	void delete(Long noticeId);
	
	void deleteBatch(Long[] noticeIds);
}
