package io.grx.modules.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import io.grx.common.exception.RRException;
import io.grx.common.utils.Constant;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.sys.dao.SysConfigDao;
import io.grx.modules.sys.entity.SysConfigEntity;
import io.grx.modules.sys.redis.SysConfigRedis;
import io.grx.modules.sys.service.SysConfigService;

@Service("sysConfigService")
public class SysConfigServiceImpl implements SysConfigService {
	@Autowired
	private SysConfigDao sysConfigDao;
	@Autowired
	private SysConfigRedis sysConfigRedis;

	@Override
	@Transactional
	public void save(SysConfigEntity config) {
		sysConfigDao.save(config);
		sysConfigRedis.saveOrUpdate(config);
	}

	@Override
	@Transactional
	public void update(SysConfigEntity config) {
		sysConfigDao.update(config);
		sysConfigRedis.saveOrUpdate(config);
	}

	@Override
	@Transactional
	public void updateValueByKey(String key, String value) {
		String merchantNo = ShiroUtils.getMerchantNo();
		SysConfigEntity configEntity = sysConfigDao.queryByKey(merchantNo, key);
		if (configEntity == null) {
			configEntity = new SysConfigEntity();
			configEntity.setKey(key);
			configEntity.setValue(value);

			sysConfigDao.save(configEntity);
		}

		configEntity.setValue(value);
		sysConfigDao.update(configEntity);
		sysConfigRedis.delete(ShiroUtils.getMerchantNo(), key);
	}

	@Override
	@Transactional
	public void updateConfigObject(String key, Object value) {
		String merchantNo = ShiroUtils.getMerchantNo();
		SysConfigEntity configEntity = sysConfigDao.queryByKey(merchantNo, key);
		if (configEntity == null) {
			configEntity = new SysConfigEntity();
			configEntity.setKey(key);
			configEntity.setValue(new Gson().toJson(value));

			sysConfigDao.save(configEntity);
		}

		configEntity.setValue(new Gson().toJson(value));
		sysConfigDao.update(configEntity);
		sysConfigRedis.delete(ShiroUtils.getMerchantNo(), key);
	}
	@Override
	@Transactional
	public void deleteBatch(Long[] ids) {
		for(Long id : ids){
			SysConfigEntity config = queryObject(id);
			sysConfigRedis.delete(ShiroUtils.getMerchantNo(), config.getKey());
		}

		sysConfigDao.deleteBatch(ids);
	}

	@Override
	public List<SysConfigEntity> queryList(Map<String, Object> map) {
		return sysConfigDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysConfigDao.queryTotal(map);
	}

	@Override
	public SysConfigEntity queryObject(Long id) {
		return sysConfigDao.queryObject(id);
	}

	@Override
	public SysConfigEntity queryObjectBySysConfigEntity(SysConfigEntity sysConfigEntity) {
		return sysConfigDao.queryObjectBySysConfigEntity(sysConfigEntity);
	}

	@Override
	public String getValue(String key) {
		String merchantNo = ShiroUtils.getMerchantNo();
		String value = getValue(merchantNo, key);
		if (value == null) {
			value = getValue(Constant.DEFAULT_MERCHANT_NO, key);
		}
		return value;
	}

	private String getValue(String merchantNo, String key) {
		SysConfigEntity config = sysConfigRedis.get(merchantNo, key);
		if(config == null){
			config = sysConfigDao.queryByKey(merchantNo, key);
			sysConfigRedis.saveOrUpdate(config);
		}

		return config == null ? null : config.getValue();
	}


	
	@Override
	public <T> T getConfigObject(String key, Class<T> clazz) {
		String value = getValue(key);
		if(StringUtils.isNotBlank(value)){
			return new Gson().fromJson(value, clazz);
		}

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RRException("获取参数失败");
		}
	}
}
