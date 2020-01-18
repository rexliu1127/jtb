package io.grx.modules.sys.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.grx.modules.sys.entity.SysDeptEntity;
import io.grx.modules.sys.entity.SysFunEntity;
import io.grx.modules.sys.service.SysDeptService;
import io.grx.modules.sys.service.SysFunService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.common.utils.Constant;
import io.grx.common.utils.R;
import io.grx.modules.opt.entity.ChannelEntity;
import io.grx.modules.opt.service.ChannelService;
import io.grx.modules.sys.dao.SysMerchantDao;
import io.grx.modules.sys.entity.SysMerchantEntity;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.enums.AccountStatus;
import io.grx.modules.sys.service.SysMerchantService;
import io.grx.modules.sys.service.SysUserService;


@Service("sysMerchantService")
public class SysMerchantServiceImpl implements SysMerchantService {
	@Autowired
	private SysMerchantDao sysMerchantDao;

	@Autowired
    private SysUserService sysUserService;

	@Autowired
    private ChannelService channelService;

	@Autowired
    private SysDeptService sysDeptService;


    @Autowired
    private SysFunService sysFunService;

	@Override
	public SysMerchantEntity queryObject(String merchantNo){
		return sysMerchantDao.queryObject(merchantNo);
	}
	
	@Override
	public List<SysMerchantEntity> queryList(Map<String, Object> map){
		return sysMerchantDao.queryList(map);
	}

	@Override
    public  List<SysMerchantEntity> queryValidList(Map<String, Object> map){
        return sysMerchantDao.queryValidList(map);
    }

    @Override
    public  List<SysMerchantEntity> queryValidListByFunType(Map<String, Object> map){
        return sysMerchantDao.queryValidListByFunType(map);
    }

	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysMerchantDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(SysMerchantEntity sysMerchant){
		sysMerchantDao.save(sysMerchant);
	}
	
	@Override
	@Transactional
	public void update(SysMerchantEntity sysMerchant){
		sysMerchantDao.update(sysMerchant);
	}
	
	@Override
	@Transactional
	public void delete(String merchantNo){
		sysMerchantDao.delete(merchantNo);
	}
	
	@Override
	@Transactional
	public void deleteBatch(String[] merchantNos){
		sysMerchantDao.deleteBatch(merchantNos);
	}

	@Override
	@Transactional
	public R register(final String mobile, final String password, final String name, final String email) {
        if (StringUtils.isBlank(mobile)) {
            return R.error("手机号不能为空");
        }
        if (StringUtils.isBlank(password)) {
            return R.error("密码不能为空");
        }
        if (StringUtils.isBlank(email)) {
            return R.error("Email地址不能为空");
        }

        //用户信息
        SysUserEntity user = sysUserService.queryByUserName(mobile);
        if (user != null) {
            return R.error("该手机号已被注册,请直接登录");
        }

        if (StringUtils.isNotBlank(email)) {
            user = sysUserService.queryByEmail(email);
            if (user != null) {
                return R.error("该Email已被注册,请直接登录");
            }
        }

        SysMerchantEntity merchantEntity = queryObject(mobile);
        if (merchantEntity != null) {
            return R.error("该手机号已被注册,请换另一个手机号注册");
        }

        // create default dept
        SysDeptEntity deptEntity = new SysDeptEntity();
        deptEntity.setMerchantNo(mobile);
        deptEntity.setName(name);
        deptEntity.setOrderNum(0);
        sysDeptService.save(deptEntity);

        // create user
        user = new SysUserEntity();
        user.setMerchantNo(mobile);
        user.setUsername(mobile);
        user.setMobile(mobile);
        user.setEmail(email);
        user.setPassword(password);
        user.setDeptId(deptEntity.getDeptId());
        user.setStatus(1);
        user.setCreateUserId(Long.valueOf(Constant.SUPER_ADMIN));
        user.setRoleIdList(Arrays.asList(Constant.ADMIN_ROLE_ID));
        sysUserService.save(user);

        // create merchant
        merchantEntity = new SysMerchantEntity();
        merchantEntity.setMerchantNo(mobile);
        merchantEntity.setName(name);
        merchantEntity.setAdminUserId(user.getUserId());
        merchantEntity.setCreateTime(new Date());
        merchantEntity.setStatus(AccountStatus.ENABLED);
        save(merchantEntity);

        //新增商户账户资金信息
        BigDecimal  amt = new BigDecimal(0);
        SysFunEntity newFun = new SysFunEntity();
        newFun.setMerchantNo(mobile);
        newFun.setTotalAmount(amt);  //总金额
        newFun.setRemainingSum(amt);  //可用金额
        newFun.setCreateTime(new Date());
        newFun.setMerchantName(name);
        newFun.setIsrist(1);  //1:无风险   0:有风险
        sysFunService.save(newFun);

        // create channel
        ChannelEntity channelEntity = new ChannelEntity();
        channelEntity.setName(name);
        channelEntity.setDeptId(deptEntity.getDeptId());
        channelEntity.setMerchantNo(mobile);
        channelService.save(channelEntity);

        return R.ok();
	}

}
