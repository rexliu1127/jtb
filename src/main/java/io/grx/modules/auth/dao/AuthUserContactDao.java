package io.grx.modules.auth.dao;

import io.grx.modules.auth.entity.AuthUserContactEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通讯录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-20 22:35:22
 */
@Mapper
public interface AuthUserContactDao extends BaseDao<AuthUserContactEntity> {
	
}
