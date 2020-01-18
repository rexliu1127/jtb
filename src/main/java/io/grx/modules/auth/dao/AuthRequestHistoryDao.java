package io.grx.modules.auth.dao;

import io.grx.modules.auth.entity.AuthRequestHistoryEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 申请单历史
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:18
 */
@Mapper
public interface AuthRequestHistoryDao extends BaseDao<AuthRequestHistoryEntity> {
	List<AuthRequestHistoryEntity> queryHistories(Long requestId);
}
