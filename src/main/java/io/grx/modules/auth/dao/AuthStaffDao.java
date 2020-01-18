package io.grx.modules.auth.dao;

import io.grx.modules.auth.entity.AuthStaffEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客服人员
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-01 00:36:13
 */
@Mapper
public interface AuthStaffDao extends BaseDao<AuthStaffEntity> {

    List<AuthStaffEntity> queryByProcessorId(@Param(value = "processorId") Long processorId);
}
