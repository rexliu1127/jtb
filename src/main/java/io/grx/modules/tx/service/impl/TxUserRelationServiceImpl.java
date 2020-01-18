package io.grx.modules.tx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.grx.modules.tx.dao.TxUserRelationDao;
import io.grx.modules.tx.entity.TxUserRelationEntity;
import io.grx.modules.tx.service.TxUserRelationService;


@Service("txUserRelationService")
public class TxUserRelationServiceImpl implements TxUserRelationService {
	@Autowired
	private TxUserRelationDao txUserRelationDao;
	
	@Override
	public TxUserRelationEntity queryObject(Long relationId){
		return txUserRelationDao.queryObject(relationId);
	}
	
	@Override
	public List<TxUserRelationEntity> queryList(Map<String, Object> map){
		return txUserRelationDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return txUserRelationDao.queryTotal(map);
	}
	
	@Override
	public void save(TxUserRelationEntity txUserRelation){
		txUserRelationDao.save(txUserRelation);
	}
	
	@Override
	public void update(TxUserRelationEntity txUserRelation){
		txUserRelationDao.update(txUserRelation);
	}
	
	@Override
	public void delete(Long relationId){
		txUserRelationDao.delete(relationId);
	}
	
	@Override
	public void deleteBatch(Long[] relationIds){
		txUserRelationDao.deleteBatch(relationIds);
	}
	
}
