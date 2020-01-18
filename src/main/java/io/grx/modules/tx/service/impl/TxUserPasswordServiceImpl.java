package io.grx.modules.tx.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.common.utils.UUIDGenerator;
import io.grx.modules.tx.dao.TxUserPasswordDao;
import io.grx.modules.tx.entity.TxUserPasswordEntity;
import io.grx.modules.tx.service.TxUserPasswordService;


@Service("txUserPasswordService")
public class TxUserPasswordServiceImpl implements TxUserPasswordService {
	@Autowired
	private TxUserPasswordDao txUserPasswordDao;
	
	@Override
	public TxUserPasswordEntity queryObject(Long userId){
		return txUserPasswordDao.queryObject(userId);
	}
	
	@Override
	public List<TxUserPasswordEntity> queryList(Map<String, Object> map){
		return txUserPasswordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return txUserPasswordDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(TxUserPasswordEntity txUserPassword){
		txUserPasswordDao.save(txUserPassword);
	}
	
	@Override
	@Transactional
	public void update(TxUserPasswordEntity txUserPassword){
		txUserPasswordDao.update(txUserPassword);
	}
	
	@Override
	@Transactional
	public void delete(Long userId){
		txUserPasswordDao.delete(userId);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] userIds){
		txUserPasswordDao.deleteBatch(userIds);
	}

    @Override
    public boolean isPasswordMatched(final Long userId, final String password) {
        TxUserPasswordEntity passwordEntity = queryObject(userId);
        return passwordEntity != null && StringUtils.equalsIgnoreCase(passwordEntity.getPassword(),
                new Sha256Hash(password, passwordEntity.getSalt()).toHex());
    }

	@Override
	public void saveOrUpdatePassword(final Long userId, final String password) {
		TxUserPasswordEntity passwordEntity = queryObject(userId);
		if (passwordEntity == null) {
			passwordEntity = new TxUserPasswordEntity();
			passwordEntity.setUserId(userId);

			String salt = UUIDGenerator.getUUID();
			passwordEntity.setSalt(salt);
			passwordEntity.setPassword(hashPassword(password, salt));
			save(passwordEntity);
		} else {
			passwordEntity.setPassword(hashPassword(password, passwordEntity.getSalt()));
			update(passwordEntity);
		}
	}

    private String hashPassword(final String password, final String salt) {
        return new Sha256Hash(password, salt).toHex();
    }

/*
    public static void main (String[] args) {
		System.out.println(new TxUserPasswordServiceImpl().hashPassword("123456",
				"71e66bfdf7764298b40cff042076c939"));
	}*/
}
