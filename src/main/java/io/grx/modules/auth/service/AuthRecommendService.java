package io.grx.modules.auth.service;

import io.grx.modules.auth.entity.AuthRecommendEntity;

import java.util.List;
import java.util.Map;

/**
 * 申请单推荐记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-09-28 22:57:07
 */
public interface AuthRecommendService {
	
	AuthRecommendEntity queryObject(Long id);
	
	List<AuthRecommendEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AuthRecommendEntity authRecommend);
	
	void update(AuthRecommendEntity authRecommend);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
