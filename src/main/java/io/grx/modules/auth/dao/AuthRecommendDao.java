package io.grx.modules.auth.dao;

import io.grx.modules.auth.entity.AuthRecommendEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 申请单推荐记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-09-28 22:57:07
 */
@Mapper
public interface AuthRecommendDao extends BaseDao<AuthRecommendEntity> {
	
}
