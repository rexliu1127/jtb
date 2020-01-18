package io.grx.modules.tx.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import io.grx.common.validator.ValidatorUtils;
import io.grx.common.validator.group.AddGroup;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.sys.controller.AbstractController;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.entity.TxUserVO;
import io.grx.modules.tx.enums.TxStatus;
import io.grx.modules.tx.enums.UsageType;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxUserService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.grx.modules.tx.entity.TxLenderEntity;
import io.grx.modules.tx.service.TxLenderService;
import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;

import javax.servlet.http.HttpServletRequest;

import static java.time.temporal.ChronoUnit.DAYS;


/**
 * 出借人
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-06 14:48:57
 */
@RestController
@RequestMapping("/tx/txlender")
public class TxLenderController extends AbstractController {

	private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Autowired
	private TxBaseService txBaseService;

	@Autowired
	private TxLenderService txLenderService;

	@Autowired
	private TxUserService txUserService;

	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private AuthRequestService authRequestService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("auth:txlender:list")
	public R list(@RequestParam Map<String, Object> params){

		//商户号
		String merchantno = getMerchantNo();
		params.put("merchantno",merchantno);

		//查询列表数据
        Query query = new Query(params);

		List<TxLenderEntity> txLenderList = txLenderService.queryList(query);
		int total = txLenderService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(txLenderList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("auth:txlender:info")
	public R info(@PathVariable("id") Long id){
		TxLenderEntity txLender = txLenderService.queryObject(id);
		
		return R.ok().put("txLender", txLender);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("auth:txlender:save")
	public R save(@RequestBody TxLenderEntity txLender){

		ValidatorUtils.validateEntity(txLender, AddGroup.class);

		if (StringUtils.isNotBlank(txLender.getName())&& StringUtils.isNotBlank(txLender.getMobile())) {
			TxLenderEntity lenderInDB = txLenderService.queryByMobile(txLender.getMobile());
			if(lenderInDB != null) {
				return R.error("已有其他用户使用该手机绑定出借人，请检查电话是否正确!");
			}
		}

		//根据手机号码查询借款人用户信息(tx_user)
		TxUserEntity txuser = txUserService.queryByMobile(txLender.getMobile());
		if(txuser == null) {
			return R.error("该手机还没在凭证平台注册!");
		}

		if (!StringUtils.equalsIgnoreCase(txLender.getName(), txuser.getName())) {
			return R.error("姓名和凭证平台实名用户不一致!");
		}

		txLender.setMerchantNo(getMerchantNo());
		txLender.setCreateTime(new Date());
		txLenderService.save(txLender);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("auth:txlender:update")
	public R update(@RequestBody TxLenderEntity txLender){
		ValidatorUtils.validateEntity(txLender, AddGroup.class);
		if (StringUtils.isNotBlank(txLender.getName())&& StringUtils.isNotBlank(txLender.getMobile())){
			TxLenderEntity lenderInDB = txLenderService.queryByMobile(txLender.getMobile());
			if(lenderInDB != null && !lenderInDB.getId().equals(txLender.getId())) {
				return R.error("已有其他用户使用该手机绑定出借人，请检查电话是否正确!");
			}
		}

		txLender.setMerchantNo(getMerchantNo());
		txLender.setCreateTime(new Date());

		txLenderService.update(txLender);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("auth:txlender:delete")
	public R delete(@RequestBody Long[] ids){
		txLenderService.deleteBatch(ids);
		
		return R.ok();
	}

	/**
	 *  借款人管理-打借条 ； 申请详情打借条
	 */
	@RequestMapping(value = "/transaction", method = RequestMethod.POST)
	public R transaction(Long rowID,Long borrowerID ,String amount, String beginDate, String repayDate,String borrowername,
						 String rate, Integer usageType, HttpServletRequest request){
		//查询出借人
		TxLenderEntity txLender = txLenderService.queryObject(rowID);
		if (StringUtils.isBlank(amount)) {
			return R.error("借款金额不能为空");
		}

		int txAmount = NumberUtils.toInt(amount, -1);
		if (txAmount <= 0) {
			return R.error("借条金额必须大于0");
		}

		if(beginDate==null)
		{
			LocalDate now = LocalDate.now();
			beginDate = now.format(format);
		}

		if(repayDate == null)
		{
			LocalDate now = LocalDate.now();
			repayDate = now.format(format);
		}

		LocalDate beginLocalDate = LocalDate.parse(beginDate, format);
		LocalDate endLocalDate = LocalDate.parse(repayDate, format);
		if (DAYS.between(beginLocalDate, endLocalDate) < 0) {
			return R.error("结束日期不能早于开始日期");
		}

		//根据手机号码查询出借人用户信息(tx_user)
		TxUserEntity txuser = txUserService.queryByMobile(txLender.getMobile()) ;
		if(txuser == null)
		{
			return R.error("手机号码绑定用户信息不存在!");
		}
		SysUserEntity sysuser = getUser();

		TxBaseEntity entity = new TxBaseEntity();
		BigDecimal txFee = txBaseService.getTransactionFee();

		String txUuid = txBaseService.getNewTxUuid();
		entity.setTxUuid(txUuid);
		entity.setAmount(txAmount);
		entity.setRate(NumberUtils.toInt(rate, 0));

		entity.setBeginDate(Date.from(beginLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		entity.setEndDate(Date.from(endLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		entity.setRemark("");
		entity.setUsageType(UsageType.valueOf(usageType));
		entity.setUsageRemark("");
		entity.setCreateUserId(txuser.getUserId()); //创建人必须是出借人ID 或者 借款人ID
		entity.setCreateTime(new Date());

		entity.setLenderUserId(txuser.getUserId());
		if(borrowerID!=null)  //借款人信息
		{
			//查找借款申请人用户信息
			AuthUserEntity authUser = authUserService.queryObject(borrowerID);
			entity.setBorrowerName(authUser.getName());
			entity.setBorrowerUserId(authUser.getUserId());
		}
		else
		{
			if(borrowername!=null)
			{
				entity.setBorrowerName(borrowername.trim());
			}

		}

		entity.setLenderName(txuser.getName());
		entity.setLenderSignImgPath(txuser.getSignImgPath());
		entity.setStatus(TxStatus.NEW);

		if (txBaseService.isFreeTxLenderMobile(txLender.getMobile())) {
				txFee = BigDecimal.ZERO;
			}

		entity.setInterest(txBaseService.calculateInterest(entity));
		entity.setFeeAmount(txFee);
		txBaseService.save(entity);

		return R.ok();
	}

	/**
	 * 查询商户下的出借人
	 * @return
	 */
	@RequestMapping(value = "/txlenderList")
	public R select(Long userId){

		List<TxLenderEntity>  txlenderList = new ArrayList<TxLenderEntity>();
		//查找借款申请人用户信息
		AuthUserEntity authUser = authUserService.queryObject(userId);
		if(authUser!=null)
		{
			String merchantNo = authUser.getMerchantNo(); //商户号

			Map<String, Object> map = new HashMap<>();
			map.put("merchantNo",merchantNo);
			Query query = new Query(map);
			//查询出借人
			txlenderList = txLenderService.queryListByMerchantNo(query);
		}

		return R.ok().put("list", txlenderList);
	}

	/**
	 * 自动检索借款人
	 */
	@RequestMapping(value = "/loadUserList")
	public R loadUserList(String userName){

		List<TxUserVO>  borrowerList = new ArrayList<TxUserVO>();
		String merchantNo = getMerchantNo();
		Map<String, Object> map = new HashMap<>();
		map.put("merchantNo",merchantNo);
		map.put("userName",userName);
		Query query = new Query(map);
		List<TxUserEntity> txlenderList = txUserService.getUserListByMerchantNo(query);
		if(!txlenderList.isEmpty())
		{
               for(TxUserEntity  user : txlenderList)
			   {
				   TxUserVO    uservo = new TxUserVO();
				   uservo.setLabel(user.getName());
				   uservo.setValue(user.getName());

				   borrowerList.add(uservo);
			   }
		}
		return R.ok().put("list", borrowerList);
	}

}
