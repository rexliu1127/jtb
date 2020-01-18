package io.grx.modules.tx.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.tx.dao.TxUserBalanceLogDao;
import io.grx.modules.tx.entity.TxUserBalanceLogEntity;
import io.grx.modules.tx.service.TxUserBalanceLogService;



@Service("txUserBalanceLogService")
public class TxUserBalanceLogServiceImpl implements TxUserBalanceLogService {
	@Autowired
	private TxUserBalanceLogDao txUserBalanceLogDao;
	
	@Override
	public TxUserBalanceLogEntity queryObject(Long id){
		return txUserBalanceLogDao.queryObject(id);
	}
	
	@Override
	public List<TxUserBalanceLogEntity> queryList(Map<String, Object> map){
		return txUserBalanceLogDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return txUserBalanceLogDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(TxUserBalanceLogEntity txUserBalanceLog){
		txUserBalanceLogDao.save(txUserBalanceLog);
	}
	
	@Override
    @Transactional
	public void update(TxUserBalanceLogEntity txUserBalanceLog){
		txUserBalanceLogDao.update(txUserBalanceLog);
	}
	
	@Override
    @Transactional
	public void delete(Long id){
		txUserBalanceLogDao.delete(id);
	}
	
	@Override
    @Transactional
	public void deleteBatch(Long[] ids){
		txUserBalanceLogDao.deleteBatch(ids);
	}
	
}
