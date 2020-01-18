package io.grx.modules.sys.service.impl;

import io.grx.common.utils.Constant;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.sys.service.SysFunTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.grx.common.utils.Query;
import io.grx.modules.sys.dao.SysFunDetailsDao;
import io.grx.modules.sys.entity.SysFunDetailsEntity;
import io.grx.modules.sys.service.SysFunDetailsService;
import io.grx.modules.sys.entity.SysFunEntity;
import io.grx.modules.sys.entity.SysFunTypeEntity;
import io.grx.modules.sys.service.SysFunService;

@Service("sysFunDetailsService")
public class SysFunDetailsServiceImpl implements SysFunDetailsService {
	@Autowired
	private SysFunDetailsDao sysFunDetailsDao;

	@Autowired
	private SysFunTypeService sysFunTypeService;

	@Autowired
	private SysFunService sysFunService;

	@Override
	public SysFunDetailsEntity queryObject(Long id){
		return sysFunDetailsDao.queryObject(id);
	}
	
	@Override
	public List<SysFunDetailsEntity> queryList(Map<String, Object> map){
		return sysFunDetailsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysFunDetailsDao.queryTotal(map);
	}
	
	@Override
	public void save(SysFunDetailsEntity sysFunDetails){
		sysFunDetailsDao.save(sysFunDetails);
	}
	
	@Override
	public void update(SysFunDetailsEntity sysFunDetails){
		sysFunDetailsDao.update(sysFunDetails);
	}
	
	@Override
	public void delete(Long id){
		sysFunDetailsDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		sysFunDetailsDao.deleteBatch(ids);
	}

	@Override
	public SysFunDetailsEntity  queryInfoByTaskID(String taskID){
		return sysFunDetailsDao.queryInfoByTaskID(taskID);
	}


	/**
	 * 新增账户消费明细
	 * @param authUser
	 * @return
	 */
	public int saveFunDetailsByDuoTou(AuthUserEntity authUser)
	{
		String taskID = authUser.getAuthTaskId();
		SysFunDetailsEntity  fundetails = queryInfoByTaskID(taskID);

		//本笔交易是否成功扣费过
		if(fundetails != null  && fundetails.getTaskId()!=null)
		{
			//成功扣费过
			return   -1;
		}
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("merchantNo",authUser.getMerchantNo());
		params.put("funType", Constant.FUN_TYPE_DUOTOU);
		Query query = new Query(params);
		//查询消费类型信息
		SysFunTypeEntity   funtype =  sysFunTypeService.queryObjectByConditions(query);
		if (funtype != null) {
			BigDecimal free = funtype.getSingleFee();

			//根据商户编号查询账户总览记录
			SysFunEntity fun = sysFunService.queryinfo(authUser.getMerchantNo());
			//根据商户编号和计算类型  查询单笔扣费金额,扣除可用余额
			fun.setRemainingSum(fun.getRemainingSum().subtract(free).setScale(2, BigDecimal.ROUND_HALF_UP));
			//扣除商户的可用余额
			sysFunService.update(fun);

			//新增扣费明细
			SysFunDetailsEntity fundetail = new SysFunDetailsEntity();
			fundetail.setUserId(authUser.getUserId());
			fundetail.setBorrowerPhone(authUser.getMobile());
			fundetail.setFunType(Constant.FUN_TYPE_DUOTOU);  //多头报告扣费
			fundetail.setAmount(free);
			fundetail.setUserName(authUser.getName());
			fundetail.setMerchantNo(authUser.getMerchantNo());
			fundetail.setCreateTime(new Date());
			fundetail.setTaskId(taskID);  //交易编号
			this.save(fundetail);
		}
		return 0;
	}
	
}
