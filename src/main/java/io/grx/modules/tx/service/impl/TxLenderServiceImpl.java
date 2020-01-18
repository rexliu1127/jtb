package io.grx.modules.tx.service.impl;

import io.grx.common.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.grx.modules.tx.dao.TxLenderDao;
import io.grx.modules.tx.entity.TxLenderEntity;
import io.grx.modules.tx.service.TxLenderService;
import org.springframework.transaction.annotation.Transactional;


@Service("txLenderService")
public class TxLenderServiceImpl implements TxLenderService {
	@Autowired
	private TxLenderDao txLenderDao;

	@Override
	public TxLenderEntity queryObject(Long id){
		return txLenderDao.queryObject(id);
	}
	
	@Override
	public List<TxLenderEntity> queryList(Map<String, Object> map){
		return txLenderDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return txLenderDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(TxLenderEntity txLender){
		txLenderDao.save(txLender);
	}
	
	@Override
	@Transactional
	public void update(TxLenderEntity txLender){
		txLenderDao.update(txLender);
	}
	
	@Override
	@Transactional
	public void delete(Long id){
		txLenderDao.delete(id);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		txLenderDao.deleteBatch(ids);
	}

	@Override
	public TxLenderEntity queryByMobile(String mobile)
	{
		 return txLenderDao.queryByMobile(mobile);
	}

	@Override
	public List<TxLenderEntity> queryListByMerchantNo(Map<String, Object> map)
	{
		return txLenderDao.queryListByMerchantNo(map);
	}


	@Override
	public List<String> getAllLenderMobiles() {
		List<String> result = new ArrayList<>();
		result.add(ShiroUtils.getMerchantNo());
		result.addAll(txLenderDao.getAllLenderMobiles());
		return result;
	}
}
