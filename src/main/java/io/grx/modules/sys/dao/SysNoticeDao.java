package io.grx.modules.sys.dao;

import io.grx.modules.sys.entity.SysNoticeEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公告表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-20 10:13:19
 */
@Mapper
public interface SysNoticeDao extends BaseDao<SysNoticeEntity> {
	
}
