package io.grx.modules.sys.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.grx.common.utils.Constant;
import io.grx.common.utils.R;
import io.grx.modules.sys.dao.SysUserTokenDao;
import io.grx.modules.sys.entity.SysUserTokenEntity;
import io.grx.modules.sys.oauth2.TokenGenerator;
import io.grx.modules.sys.service.SysUserTokenService;


@Service("sysUserTokenService")
public class SysUserTokenServiceImpl implements SysUserTokenService {
	@Autowired
	private SysUserTokenDao sysUserTokenDao;
	//12小时后过期
	private final static int EXPIRE = 3600 * 12;

	private final static String INTEGRATION_TOKEN = "18a1e3a33b9d802cd5c782fc92a316be";


	@Value("${login.allowConcurrentLogin:false}")
	private boolean allowConcurrentLogin;

	@Override
	public SysUserTokenEntity queryByUserId(Long userId) {
		return sysUserTokenDao.queryByUserId(userId);
	}

	@Override
	public void save(SysUserTokenEntity token){
		sysUserTokenDao.save(token);
	}
	
	@Override
	public void update(SysUserTokenEntity token){
		sysUserTokenDao.update(token);
	}

	@Override
	public R createToken(long userId) {
		return createToken(userId, EXPIRE);
	}

	/**
	 * 生成token
	 *
	 * @param userId          用户ID
	 * @param expireInSeconds
	 */
	@Override
	public R createToken(final long userId, final long expireInSeconds) {
		//生成一个token
		String token = TokenGenerator.generateValue();
		if (userId == 2) {
			token = INTEGRATION_TOKEN;
		}

		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + expireInSeconds * 1000);

		SysUserTokenEntity tokenEntity = null;
		if (!allowConcurrentLogin) {
			//判断是否生成过token
			tokenEntity = queryByUserId(userId);
		}

		if(tokenEntity == null){
			tokenEntity = new SysUserTokenEntity();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//保存token
			save(tokenEntity);
		}else{
			if (Constant.SUPER_ADMIN == userId && tokenEntity.getExpireTime().getTime() > System
					.currentTimeMillis()) {
				token = tokenEntity.getToken();
			}

			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);


			//更新token
			update(tokenEntity);
		}

		R r = R.ok().put("token", token).put("expire", EXPIRE);

		return r;
	}

	@Override
	public void logout(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//修改token
		SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
		tokenEntity.setUserId(userId);
		tokenEntity.setToken(token);
		update(tokenEntity);
	}
}
