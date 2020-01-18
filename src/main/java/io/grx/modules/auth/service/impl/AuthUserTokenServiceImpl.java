package io.grx.modules.auth.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.auth.dao.AuthUserTokenDao;
import io.grx.modules.auth.entity.AuthUserTokenEntity;
import io.grx.modules.auth.service.AuthUserTokenService;
import io.grx.modules.sys.oauth2.TokenGenerator;


@Service("authUserTokenService")
public class AuthUserTokenServiceImpl implements AuthUserTokenService {
    private static final int EXPIRE = 60 * 60 * 24 * 7; //一个小时

	@Autowired
	private AuthUserTokenDao authUserTokenDao;
	
	@Override
	public AuthUserTokenEntity queryObject(Long userId){
		return authUserTokenDao.queryObject(userId);
	}
	
	@Override
	public List<AuthUserTokenEntity> queryList(Map<String, Object> map){
		return authUserTokenDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return authUserTokenDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(AuthUserTokenEntity authUserToken){
		authUserTokenDao.save(authUserToken);
	}
	
	@Override
	@Transactional
	public void update(AuthUserTokenEntity authUserToken){
		authUserTokenDao.update(authUserToken);
	}
	
	@Override
	@Transactional
	public void delete(Long userId){
		authUserTokenDao.delete(userId);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] userIds){
		authUserTokenDao.deleteBatch(userIds);
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
        AuthUserTokenEntity tokenEntity = authUserTokenDao.queryByUser(userId);
        if(tokenEntity == null){
            tokenEntity = new AuthUserTokenEntity();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //保存token
            save(tokenEntity);
        }else{
            tokenEntity.setToken(token);
//			token = tokenEntity.getToken();
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
        AuthUserTokenEntity tokenEntity = new AuthUserTokenEntity();
        tokenEntity.setUserId(userId);
        tokenEntity.setToken(token);
        update(tokenEntity);
	}

    @Override
	@Transactional
    public Long getUserIdByToken(final String token) {
	    AuthUserTokenEntity tokenEntity = authUserTokenDao.queryByToken(token);
	    if (tokenEntity != null && tokenEntity.getExpireTime() != null
                && tokenEntity.getExpireTime().getTime() > System.currentTimeMillis()) {

	    	if (tokenEntity.getExpireTime().getTime() - System.currentTimeMillis() < 10 * 60 * 1000)  {
	    		// if token will be expired in 10 minutes, auto extend
				tokenEntity.setExpireTime(new Date(System.currentTimeMillis() + EXPIRE * 1000));
				update(tokenEntity);
			}
	        return tokenEntity.getUserId();
        }
        return null;
    }
    
    @Override
    public AuthUserTokenEntity queryByToken(String token) {
    	return this.authUserTokenDao.queryByToken(token);
    }

}
