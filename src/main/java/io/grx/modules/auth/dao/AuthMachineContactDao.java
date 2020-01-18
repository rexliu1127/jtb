package io.grx.modules.auth.dao;

import io.grx.modules.auth.entity.AuthMachineContactEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * APP机器通讯录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-10-22 14:27:10
 */
@Mapper
public interface AuthMachineContactDao extends BaseDao<AuthMachineContactEntity> {
	
}
