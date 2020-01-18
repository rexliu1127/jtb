package io.grx.modules.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.grx.modules.auth.dao.AuthRecommendDao;
import io.grx.modules.auth.entity.AuthRecommendEntity;
import io.grx.modules.auth.service.AuthRecommendService;



@Service("authRecommendService")
public class AuthRecommendServiceImpl implements AuthRecommendService {
	@Autowired
	private AuthRecommendDao authRecommendDao;
	
	@Override
	public AuthRecommendEntity queryObject(Long id){
		return authRecommendDao.queryObject(id);
	}
	
	@Override
	public List<AuthRecommendEntity> queryList(Map<String, Object> map){
		return authRecommendDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return authRecommendDao.queryTotal(map);
	}
	
	@Override
	public void save(AuthRecommendEntity authRecommend){
		authRecommendDao.save(authRecommend);
	}
	
	@Override
	public void update(AuthRecommendEntity authRecommend){
		authRecommendDao.update(authRecommend);
	}
	
	@Override
	public void delete(Long id){
		authRecommendDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		authRecommendDao.deleteBatch(ids);
	}
	
}
