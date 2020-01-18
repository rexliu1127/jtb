package io.grx.modules.tx.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.sys.oauth2.TokenGenerator;
import io.grx.modules.tx.dao.TxUserTokenDao;
import io.grx.modules.tx.entity.TxUserTokenEntity;
import io.grx.modules.tx.service.TxUserTokenService;



@Service("txUserTokenService")
public class TxUserTokenServiceImpl implements TxUserTokenService {
	private static final int EXPIRE = 60 * 60 * 24 * 7; //一个星期

	@Autowired
	private TxUserTokenDao txUserTokenDao;
	
	@Override
	public TxUserTokenEntity queryObject(Long userId){
		return txUserTokenDao.queryObject(userId);
	}
	
	@Override
	public List<TxUserTokenEntity> queryList(Map<String, Object> map){
		return txUserTokenDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return txUserTokenDao.queryTotal(map);
	}
	
	@Override
    @Transactional
	public void save(TxUserTokenEntity txUserToken){
		txUserTokenDao.save(txUserToken);
	}
	
	@Override
    @Transactional
	public void update(TxUserTokenEntity txUserToken){
		txUserTokenDao.update(txUserToken);
	}
	
	@Override
    @Transactional
	public void delete(Long userId){
		txUserTokenDao.delete(userId);
	}
	
	@Override
    @Transactional
	public void deleteBatch(Long[] userIds){
		txUserTokenDao.deleteBatch(userIds);
	}


	/**
	 * 生成token
	 *
	 * @param userId 用户ID
	 */
	@Override
    @Transactional
	public String createToken(final long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		//判断是否生成过token
		TxUserTokenEntity tokenEntity = txUserTokenDao.queryByUser(userId);
		if(tokenEntity == null){
			tokenEntity = new TxUserTokenEntity();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//保存token
			save(tokenEntity);
		}else{
//			tokenEntity.setToken(token);
			token = tokenEntity.getToken();
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//更新token
			update(tokenEntity);
		}

		return token;
	}

	/**
	 * 退出，修改token值
	 *
	 * @param userId 用户ID
	 */
	@Override
    @Transactional
	public void logout(final long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//修改token
		TxUserTokenEntity tokenEntity = new TxUserTokenEntity();
		tokenEntity.setUserId(userId);
		tokenEntity.setToken(token);
		update(tokenEntity);
	}

	@Override
	public Long getUserIdByToken(final String token) {
		TxUserTokenEntity tokenEntity = txUserTokenDao.queryByToken(token);
		if (tokenEntity != null && tokenEntity.getExpireTime() != null
				&& tokenEntity.getExpireTime().getTime() > System.currentTimeMillis()) {
			return tokenEntity.getUserId();
		}
		return null;
	}
}
