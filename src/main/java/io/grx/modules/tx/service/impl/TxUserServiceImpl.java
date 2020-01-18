package io.grx.modules.tx.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.tx.dao.TxUserDao;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.dao.TxUserRelationDao;
import io.grx.modules.tx.service.TxUserService;



@Service("txUserService")
public class TxUserServiceImpl implements TxUserService {
	@Autowired
	private TxUserDao txUserDao;

	@Autowired
	private TxUserRelationDao txUserRelationDao;
	
	@Override
	public TxUserEntity queryObject(Long userId){
		return txUserDao.queryObject(userId);
	}

	@Override
	public TxUserEntity queryByWechatId(final String weChatId) {
		return txUserDao.queryByWechatId(weChatId);
	}

	@Override
	public TxUserEntity queryByMobile(final String mobile) {
		return txUserDao.queryByMobile(mobile);
	}

	@Override
	public TxUserEntity queryByUnionId(final String unionId) {
		return txUserDao.queryByUnionId(unionId);
	}

	@Override
	public List<TxUserEntity> queryByIdNo(final String idNo) {
		return txUserDao.queryByIdNo(idNo);
	}

	@Override
	public List<TxUserEntity> queryList(Map<String, Object> map){
		return txUserDao.queryList(map);
	}

	@Override
	public List<TxUserEntity> getUserListByMerchantNo(Map<String, Object> map){
		return txUserDao.getUserListByMerchantNo(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map){
		return txUserDao.queryTotal(map);
	}

	@Override
	@Transactional
	public void save(TxUserEntity txUser){
		txUserDao.save(txUser);
	}
	
	@Override
	@Transactional
	public void update(TxUserEntity txUser){
		txUserDao.update(txUser);
	}
	
	@Override
	@Transactional
	public void delete(Long userId){
		txUserDao.delete(userId);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] userIds){
		txUserDao.deleteBatch(userIds);
	}

	@Override
	public List<TxUserEntity> getFriendList(final Long userId, String userName) {
		return txUserDao.getFriendList(userId, userName);
	}

    @Override
    public int getFriendTotal(final Long userId) {
        return txUserRelationDao.getFriendTotal(userId);
    }

    @Override
    public boolean isFriend(final Long userId, final Long otherUserId) {
        return !userId.equals(otherUserId) && txUserRelationDao.hasRelation(userId, otherUserId) > 0;
    }

}
