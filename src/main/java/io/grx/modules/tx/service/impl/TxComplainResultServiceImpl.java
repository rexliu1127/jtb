package io.grx.modules.tx.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.tx.dao.TxComplainResultDao;
import io.grx.modules.tx.entity.TxComplainResultEntity;
import io.grx.modules.tx.service.TxComplainResultService;



@Service("txComplainResultService")
public class TxComplainResultServiceImpl implements TxComplainResultService {
	@Autowired
	private TxComplainResultDao txComplainResultDao;
	
	@Override
	public TxComplainResultEntity queryObject(Long id){
		return txComplainResultDao.queryObject(id);
	}
	
	@Override
	public List<TxComplainResultEntity> queryList(Map<String, Object> map){
		return txComplainResultDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return txComplainResultDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(TxComplainResultEntity txComplainResult){
		txComplainResultDao.save(txComplainResult);
	}

	@Override
	@Transactional
	public void update(TxComplainResultEntity txComplainResult){
		txComplainResultDao.update(txComplainResult);
	}

	@Override
	@Transactional
	public void delete(Long id){
		txComplainResultDao.delete(id);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		txComplainResultDao.deleteBatch(ids);
	}

	@Override
	public List<TxComplainResultEntity> queryByComplainId(final Long complainId) {
		return txComplainResultDao.queryByComplainId(complainId);
	}
}
