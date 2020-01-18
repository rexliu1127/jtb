package io.grx.modules.tx.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.tx.dao.TxComplainDao;
import io.grx.modules.tx.entity.TxComplainEntity;
import io.grx.modules.tx.service.TxComplainService;



@Service("txComplainService")
public class TxComplainServiceImpl implements TxComplainService {
	@Autowired
	private TxComplainDao txComplainDao;
	
	@Override
	public TxComplainEntity queryObject(Long complainId){
		return txComplainDao.queryObject(complainId);
	}
	
	@Override
	public List<TxComplainEntity> queryList(Map<String, Object> map){
		return txComplainDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return txComplainDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(TxComplainEntity txComplain){
		txComplainDao.save(txComplain);
	}
	
	@Override
	@Transactional
	public void update(TxComplainEntity txComplain){
		txComplainDao.update(txComplain);
	}
	
	@Override
	@Transactional
	public void delete(Long complainId){
		txComplainDao.delete(complainId);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] complainIds){
		txComplainDao.deleteBatch(complainIds);
	}

    @Override
    public TxComplainEntity getLatestByTxId(final Long txId) {
        return txComplainDao.getLatestByTxId(txId);
    }

}
