package io.grx.modules.tx.service.impl;

import io.grx.modules.tx.dto.TxUserInviteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.grx.modules.tx.dao.TxUserInviteDao;
import io.grx.modules.tx.entity.TxUserInviteEntity;
import io.grx.modules.tx.service.TxUserInviteService;
import org.springframework.transaction.annotation.Transactional;


@Service("txUserInviteService")
public class TxUserInviteServiceImpl implements TxUserInviteService {
	@Autowired
	private TxUserInviteDao txUserInviteDao;
	
	@Override
	public TxUserInviteEntity queryObject(Long id){
		return txUserInviteDao.queryObject(id);
	}
	
	@Override
	public List<TxUserInviteEntity> queryList(Map<String, Object> map){
		return txUserInviteDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return txUserInviteDao.queryTotal(map);
	}

	@Override
	@Transactional
	public void save(TxUserInviteEntity txUserInvite){
		txUserInviteDao.save(txUserInvite);
	}

	@Override
	@Transactional
	public void update(TxUserInviteEntity txUserInvite){
		txUserInviteDao.update(txUserInvite);
	}

	@Override
	@Transactional
	public void delete(Long id){
		txUserInviteDao.delete(id);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		txUserInviteDao.deleteBatch(ids);
	}

	@Override
	public List<TxUserInviteVO> queryUserInviteList(Long userId, Integer level) {
		return txUserInviteDao.queryUserInviteList(userId, level);
	}

	@Override
	public TxUserInviteEntity queryByUseId(Long userId) {
		return txUserInviteDao.queryByUseId(userId);
	}

	@Override
	public int getTeamUserCount(Long userId) {
		return txUserInviteDao.getTeamUserCount(userId);
	}

}
