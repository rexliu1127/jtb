package io.grx.modules.sys.service.impl;

import io.grx.modules.sys.entity.SysConfigEntity;
import io.grx.modules.sys.entity.SysFunEntity;
import io.grx.modules.sys.entity.SysMerchantEntity;
import io.grx.modules.sys.service.SysConfigService;
import io.grx.modules.sys.service.SysMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import io.grx.modules.sys.service.SysFunService;
import io.grx.modules.sys.dao.SysFunRecordDao;
import io.grx.modules.sys.entity.SysFunRecordEntity;
import io.grx.modules.sys.service.SysFunRecordService;
import org.springframework.transaction.annotation.Transactional;

@Service("sysFunRecordService")
public class SysFunRecordServiceImpl implements SysFunRecordService {
	private static final String KEY_YOUDUN_MOBILE = "YOU_DUN_MOBILE";
	@Autowired
	private SysFunService sysFunService;

	@Autowired
	private SysFunRecordDao sysFunRecordDao;

	@Autowired
	private SysMerchantService sysMerchantService;

	@Autowired
	private SysConfigService sysConfigService;

	@Override
	public SysFunRecordEntity queryObject(Long id){
		return sysFunRecordDao.queryObject(id);
	}
	
	@Override
	public List<SysFunRecordEntity> queryList(Map<String, Object> map){
		return sysFunRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysFunRecordDao.queryTotal(map);
	}


	/**
	 * 新增充值，同步修改账户可用余额
	 * @param sysFunRecord
	 */
	@Override
	@Transactional
	public void save(SysFunRecordEntity sysFunRecord){

		//商户编号
		String merchant_no = sysFunRecord.getMerchantNo();
		//本次充值金额
		BigDecimal  recordAmt = sysFunRecord.getFunAmount();

		//查询商户详情
		SysMerchantEntity merchant = sysMerchantService.queryObject(merchant_no);

		//根据商户编号查询账户总览记录
		SysFunEntity fun  = sysFunService.queryinfo(merchant_no);
		BigDecimal availableAmount=new BigDecimal(0);
		if(fun!=null)
		{
			//获取账户可用余额(充值前)
			availableAmount=fun.getRemainingSum();
			//变更账户总额和可用余额
			BigDecimal   total_amount = fun.getTotalAmount().add(recordAmt).setScale(2,BigDecimal.ROUND_HALF_UP);
			BigDecimal   remaining_sum  = fun.getRemainingSum().add(recordAmt).setScale(2,BigDecimal.ROUND_HALF_UP);

			fun.setTotalAmount(total_amount);
			fun.setRemainingSum(remaining_sum);
			sysFunService.update(fun);
		}
		else
		{
			SysFunEntity newFun = new SysFunEntity();
			newFun.setMerchantNo(merchant_no);
			newFun.setTotalAmount(recordAmt);
			newFun.setRemainingSum(recordAmt);
			newFun.setCreateTime(new Date());
			if(merchant!=null)
			{
				newFun.setMerchantName(merchant.getName());
			}

			newFun.setIsrist(1);  //1:无风险   0:有风险
			sysFunService.save(newFun);
		}
		//查询多头权限
		SysConfigEntity sysConfigParam=new SysConfigEntity();
		sysConfigParam.setMerchantNo("00");
		sysConfigParam.setKey(KEY_YOUDUN_MOBILE);
		SysConfigEntity sysConfigEntity=sysConfigService.queryObjectBySysConfigEntity(sysConfigParam);
		if(sysConfigEntity!=null){
			String sysConfigValue=sysConfigEntity.getValue();
			//判断参数是否配置了该商户
			if(!sysConfigValue.contains(merchant_no)){
				//未配置该商户则加入该商户
				StringBuilder stringBuilder=new StringBuilder(sysConfigValue);
				int index=stringBuilder.indexOf("]");
				String newStr=",\""+merchant_no+"\"";
				stringBuilder.insert(index,newStr);
				sysConfigEntity.setValue(stringBuilder.toString());
				sysConfigService.update(sysConfigEntity);
			}
		}
		if(merchant!=null)
		{
			sysFunRecord.setMerchantName(merchant.getName());
		}
		sysFunRecord.setAvailableAmount(availableAmount);
		sysFunRecordDao.save(sysFunRecord);
	}
	
	@Override
	public void update(SysFunRecordEntity sysFunRecord){
		sysFunRecordDao.update(sysFunRecord);
	}
	
	@Override
	public void delete(Long id){
		sysFunRecordDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		sysFunRecordDao.deleteBatch(ids);
	}
	
}
