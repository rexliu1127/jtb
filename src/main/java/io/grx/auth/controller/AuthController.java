package io.grx.auth.controller;

import java.math.BigDecimal;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import com.google.gson.Gson;
import io.grx.auth.entity.AuthConfig;
import io.grx.auth.entity.requestModel.RequestLoginModel;
import io.grx.auth.entity.requestModel.RequestRegisterModel;
import io.grx.auth.service.*;
import io.grx.auth.thread.FlowTrackRunnable;
import io.grx.common.utils.*;
import io.grx.modules.auth.entity.*;
import io.grx.modules.auth.enums.*;
import io.grx.modules.auth.service.*;
import io.grx.modules.flow.service.FlowAllocationRecordService;
import io.grx.modules.sys.entity.SysFunEntity;
import io.grx.modules.sys.entity.SysFunTypeEntity;
import io.grx.modules.sys.service.SysConfigService;
import io.grx.modules.sys.service.SysFunService;
import io.grx.modules.sys.service.SysFunTypeService;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.service.TxUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import io.grx.common.service.BankAccountService;
import io.grx.common.service.GeoService;
import io.grx.common.service.SmsCodeService;
import io.grx.modules.opt.entity.ChannelEntity;
import io.grx.modules.opt.service.ChannelService;
import io.grx.wx.annotation.WxJsSign;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthUserService authUserService;
    
    @Autowired
    private AuthRequestLoanService authRequestLoanService;

    @Autowired
    private AuthUserTokenService authUserTokenService;
    
    @Autowired
    private TxUserService txUserService;

    @Autowired
    private SmsCodeService smsCodeService;

    @Autowired
    private AuthRequestService authRequestService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ShujuMoheMobileService shujuMoheMobileService;

    @Autowired
    private AuthUserContactService authUserContactService;

    @Autowired
    private TianjiService tianjiService;

    @Autowired
    private TjReportService tjReportService;

    @Autowired
    private GeoService geoService;

    @Autowired
    private AuthStaffService authStaffService;

    @Autowired
    private YouDunService youDunService;

    @Autowired
    private AuthCreditService authCreditService;
    @Autowired
    private SysFunService sysFunService;
    @Autowired
    private SysFunTypeService sysFunTypeService;

    @Autowired
    private YiXiangService yiXiangService;

    @Autowired
    private BankAccountService bankAccountService;
    
    @Autowired
    private SysConfigService sysConfigService;
    
    @Autowired
    private FlowAllocationRecordService flowAllocationRecordService;

    @Value("${login.h5}")
    private String loginUrl;


    @RequestMapping(value = "/invite.html", method = RequestMethod.GET)
    public String invite(HttpServletRequest request) {

        ChannelEntity channel = null;
        String channelId = request.getParameter("channelId");
        if (StringUtils.isNotBlank(channelId)) {

            channel = channelService.queryByKey(channelId);

            if (channel != null && channel.getStatus() != null && channel.getStatus()  == 0) {
                channel = null;
            }

            if (channel != null) {
                ShiroUtils.setSessionAttribute(Constant.KEY_CHANNEL, channel);
                request.setAttribute(Constant.KEY_CHANNEL, channel);
            }
        }
        return "auth/invite";

    }

    /**
     * 注册
     * @param req
     * @param model
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> register(HttpServletRequest req, @RequestBody RequestRegisterModel model) {
        if (StringUtils.isBlank(model.getMobile())) {
            return R.error("手机号不能为空");
        }
        if (StringUtils.isBlank(model.getVerifyCode())) {
            return R.error("手机验证码不能为空");
        }


        if (!smsCodeService.isVerifyCodeMatch(model.getMobile(), "register", model.getVerifyCode())) {
            return R.error("手机验证码错误或已失败, 请重新获取");
        }


        Long userChannelId = ShiroUtils.getAuthChannelId();

        //2019-04-16 app进件无缘无故出现了BUG，取不到渠道信息，这里从header里面拿
        if(userChannelId==null) {
            String channelKey = req.getHeader("CHANNEL-KEY");
            if(StringUtils.isEmpty(channelKey)) {
                return R.error("错误108");
            }
            ChannelEntity channel = this.channelService.queryByKey(channelKey);
            if(channel!=null) {
                ShiroUtils.setSessionAttribute(Constant.KEY_CHANNEL, channel);
                req.setAttribute(Constant.KEY_CHANNEL, channel);
                userChannelId = channel.getChannelId();
            }
        }

        String machineId = ShiroUtils.getMachineId();
        AuthUserEntity userEntity = authUserService.queryByMobile(model.getMobile(), userChannelId);
        if (userEntity == null) {
            userEntity = new AuthUserEntity();
            userEntity.setMobile(model.getMobile());
            userEntity.setMerchantNo(ShiroUtils.getAuthChannel().getMerchantNo());
            userEntity.setChannelId(userChannelId);
            userEntity.setMachineId(machineId);

            authUserService.saveWithNewRequest(userEntity);

        } else {
            return R.error(333,"您已注册,请直接登录").put("url",loginUrl+"?channel="+ShiroUtils.getAuthChannel().getChannelKey());
        }

        return R.ok().put("url",loginUrl+"?channel="+ShiroUtils.getAuthChannel().getChannelKey());
    }


    @RequestMapping(value = "/downapp.html", method = RequestMethod.GET)
    public String downapp(HttpServletRequest request, Model model) {

        model.addAttribute("channel",request.getParameter("channel"));
        return "auth/downapp";
    }

    /**
     * 登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(HttpServletRequest req, @RequestBody RequestLoginModel model) {
        if (StringUtils.isBlank(model.getMobile())) {
            return R.error("手机号不能为空");
        }
        if (StringUtils.isBlank(model.getVerifyCode())) {
            return R.error("手机验证码不能为空");
        }

        if (StringUtils.isBlank(model.getChannel())) {
            return R.error("商户来源不正确，请联系商户!");
        }


        if (!smsCodeService.isVerifyCodeMatch(model.getMobile(), "login", model.getVerifyCode())) {
            return R.error("手机号验证码错误或已失败, 请重新获取");
        }

        ChannelEntity channel = this.channelService.queryByKey(model.getChannel());
        if(channel==null) {
            return R.error("商户来源不正确，请联系商户!");
        }


        AuthUserEntity userEntity = authUserService.queryByMobile(model.getMobile(), channel.getChannelId());
        if(userEntity == null){
            return R.error("该手机号未注册！");
        }

        String token = authUserTokenService.createToken(userEntity.getUserId());
        ShiroUtils.setAuthUser(userEntity);

        return R.ok().put("token", token);
    }



    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public R authStatuses() {
        AuthUserEntity userEntity = ShiroUtils.getAuthUser();

//        Map<String, Integer> status = new HashMap<>();
//
//        for (TianjiType type : TianjiType.values()) {
//            TjReportEntity report = tjReportService.queryLatestByUserId(userEntity.getUserId(), type);
//            status.put(type.name().toLowerCase(), report == null ? -1 : (report.isExpired() ? 0 : 1));
//        }
//
//        for (YiXiangType type : YiXiangType.values()) {
//            YxReportEntity report = yxReportService.queryLatestByUserId(userEntity.getUserId(), type);
//            status.put(type.name().toLowerCase(), report == null ? -1 : (report.isExpired() ? 0 : 1));
//        }
//
//        AuthUserContactEntity contactEntity = authUserContactService.queryObject(userEntity.getUserId());
//        status.put("contact", contactEntity != null ? 1 : -1);

        AuthRequestEntity lastRequest = authRequestService.queryLatestByUserId(userEntity.getUserId(),
                ShiroUtils.getAuthChannelId());

        AuthStaffEntity staffEntity = null;
        if (lastRequest != null) {
            List<AuthStaffEntity> authStaffEntities = authStaffService.queryByProcessorId(
                    lastRequest.getAssigneeId());
            if (CollectionUtils.isNotEmpty(authStaffEntities)) {
                staffEntity = authStaffEntities.get(0);
            }
        }

        return R.ok().put("authStatus", authCreditService.getAllCreditStatus())
                .put("request", lastRequest)
                .put("user", ShiroUtils.getAuthUser())
                .put("staff", staffEntity);
    }




    /**
     * 开始认证跳转
     *
     * @return
     */
    @RequestMapping(value = "/verify_init", method = RequestMethod.GET)
    public String verifyInit(Model model, HttpServletRequest request) {
        AuthUserEntity user = ShiroUtils.getAuthUser();

        ChannelEntity channelEntity = (ChannelEntity) request.getAttribute("CHANNEL");
        AuthRequestEntity requestEntity = authRequestService.queryLatestByUserId(user.getUserId(), channelEntity
                .getChannelId());

        if (requestEntity != null) {
            if (requestEntity.getVerifyStatus() == VerifyStatus.PROCESSING) {
                if (requestEntity.getVendorType() == AuthVendorType.SJMH && requestEntity.getVendorType() ==
                        authRequestService.getSystemVendorType()) {
                    Set<Integer> retryResultCodes = new HashSet<Integer>(Arrays.asList(101, 104, 105, 108, 122, 123, 124, 126,
                            127, 130));

                    int resultCode = shujuMoheMobileService.queryTaskResultCode(requestEntity.getVerifyToken());
                    if (resultCode == 0 || resultCode == 137) {
                        // 已成功
                        requestEntity.setVerifyStatus(VerifyStatus.SUBMITTED);
                        authRequestService.update(requestEntity);

                        return "redirect:/auth/apply.html";
                    } else if (retryResultCodes.contains(resultCode)) {
                        // 可以重试

                        return "redirect:/auth/mobile_info.html?token=" + requestEntity.getVerifyToken();
                    }
                }
            }
        }
        return "redirect:/auth/personal_info.html";
    }



    /**
     * 查询商户余额
     * @param request
     * @return
     */
    @RequestMapping(value = "/verifyBalance", method = RequestMethod.POST)
    @ResponseBody
    public R verifyBalance(HttpServletRequest request) {
        AuthUserEntity user = ShiroUtils.getAuthUser();
        String merchantNo=user.getMerchantNo();
        if(StringUtils.isNotEmpty(merchantNo)){
            //根据商户编号查询商户余额和商户类型配置单间
            SysFunEntity sysFunEntity=sysFunService.queryinfo(merchantNo);
            //查询充值类型配置
            List<SysFunTypeEntity> sysFunTypeList=sysFunTypeService.queryInfo(merchantNo);
            List singleFeeList=new ArrayList();
            if(CollectionUtils.isNotEmpty(sysFunTypeList)){
                for (SysFunTypeEntity entity : sysFunTypeList) {
                    singleFeeList.add(entity.getSingleFee());
                }
            }else{
                return R.error("该商户未配置充值费用单价请联系管理员");
            }
            BigDecimal singleFee=(BigDecimal)Collections.max(singleFeeList);
            if(sysFunEntity==null){
                return R.error("该账户未充值，请提醒管理员充值");
            }
            //判断账户余额是否大于充值配置的最大值
            if(sysFunEntity.getRemainingSum().compareTo(singleFee)==-1){//小于
                return R.error("账户余额不足，请联系管理员充值");
            }
        }else{
            return R.error("账户错误");
        }
        return R.ok();
    }
    /**
     * 认证登录页
     *
     * @return
     */
    @RequestMapping(value = "/personal_info.html", method = RequestMethod.GET)
    public String info(Model model, String action) {
        AuthUserEntity userEntity = ShiroUtils.getAuthUser();

        model.addAttribute("user", userEntity);
        model.addAttribute("action", action);

        AuthConfig authConfig = channelService.getAuthConfig();
        model.addAttribute("authConfig", authConfig);

        return "auth/personal_info_basic";
    }


    /**
     * 认证登录页
     *
     * @return
     */
    @RequestMapping(value = "/personal_info_s2.html", method = RequestMethod.GET)
    public String infoS2(Model model, String action) {
        AuthUserEntity userEntity = ShiroUtils.getAuthUser();

        model.addAttribute("user", userEntity);
        model.addAttribute("action", action);

        AuthConfig authConfig = channelService.getAuthConfig();
        model.addAttribute("authConfig", authConfig);

        return "auth/personal_info_s2";
    }

    /**
     * 认证登录页
     *
     * @return
     */
    @WxJsSign
    @RequestMapping(value = "/personal_info_id.html", method = RequestMethod.GET)
    public String infoId(Model model, String action) {
        AuthUserEntity userEntity = ShiroUtils.getAuthUser();
        ChannelEntity channelEntity = ShiroUtils.getAuthChannel();

        boolean ydEnabled = channelService.isYouDaoCreditEnabled(channelEntity);
        if (ydEnabled) {
            return "redirect:/auth/yd";
        }

        model.addAttribute("user", userEntity);
        model.addAttribute("action", action);

        AuthConfig authConfig = channelService.getAuthConfig();
        model.addAttribute("authConfig", authConfig);


        boolean isWechat = HttpContextUtils.isWechatClient(
                HttpContextUtils.getHttpServletRequest());
        model.addAttribute("isWechat", isWechat);
        if (isWechat) {
            return "auth/personal_info_id_wx";
        }
        return "auth/personal_info_id";
    }

    /**
     * 获取个人信息
     */
    @RequestMapping(value = "/personal_info", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getPersonalInfo() {
        return R.ok().put("user", ShiroUtils.getAuthUser());
    }

    /**
     * 保存个人信息
     */
    @RequestMapping(value = "/personal_info", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> savePersonalInfo(String name, String idNo, String qqNo, String wechatNo,
                                                String companyName, String companyAddr, String companyTel,
                                                String idUrl1, String idUrl2, String idUrl3, String companyJob,
                                                String salary, String sesamePoints, Integer index) {
        ChannelEntity channelEntity = ShiroUtils.getAuthChannel();
        AuthUserEntity user = ShiroUtils.getAuthUser();
        if (StringUtils.isBlank(name)) {
            return R.error("姓名不能为空");
        }
        if (StringUtils.isBlank(idNo)) {
            return R.error("身份证不能为空");
        }
        if (!IdCardUtil.isIdcard(idNo)) {
            return R.error("身份证检验错误");
        }

        user.setName(name);
        user.setIdNo(idNo);

        if (StringUtils.isNotBlank(idUrl1)) {
            user.setIdUrl1(idUrl1);
        }
        if (StringUtils.isNotBlank(idUrl2)) {
            user.setIdUrl2(idUrl2);
        }
        if (StringUtils.isNotBlank(idUrl3)) {
            user.setIdUrl3(idUrl3);
        }

        if (StringUtils.isNotBlank(qqNo)) {
            user.setQqNo(qqNo);
        }
        if (StringUtils.isNotBlank(wechatNo)) {
            user.setWechatNo(wechatNo);
        }
        if (StringUtils.isNotBlank(companyName)) {
            user.setCompanyName(companyName);
        }
        if (StringUtils.isNotBlank(companyAddr)) {
            user.setCompanyAddr(companyAddr);
        }
        if (StringUtils.isNotBlank(companyTel)) {
            user.setCompanyTel(companyTel);
        }

        if (StringUtils.isNotBlank(companyJob)) {
            user.setCompanyJob(companyJob);
        }
        if (StringUtils.isNotBlank(salary)) {
            user.setSalary(salary);
        }
        if (StringUtils.isNotBlank(sesamePoints)) {
            user.setSesamePoints(sesamePoints);
        }

        authUserService.update(user);

        updateUserDataToNewRequest(user);

        if(index!=null )   //只需第一步操作执行
        {
            //yiXiangService.saveYxReportByHuoluobo(user.getUserId(),user.getMerchantNo(),name,idNo,user.getMobile());
            if(index.intValue() == 1)
            {
                //加入线程池执行
                ThreadPoolService.getInstance().execute(new HuLuoboRunnable(yiXiangService,user.getUserId(),user.getMerchantNo(),name,idNo,user.getMobile()));
            }

        }
        
        /* 2019-04-29新增，用户保存基本信息时；跟踪相关报告|只有流量管理员商户下渠道的进件才做跟踪 */
        if(this.getFlowMerchantNo().equals(user.getMerchantNo())) {
        	Long channelId = ShiroUtils.getAuthChannelId();
            AuthRequestEntity requestEntity = authRequestService.queryLatestByUserId(user.getUserId(), channelId);
        	ThreadPoolService.getInstance().execute(new FlowTrackRunnable(requestEntity.getRequestId(),FlowTrackRunnable.SYNC_BASE,this.flowAllocationRecordService));
        }
        
        return R.ok();
    }
    
    private String getFlowMerchantNo() {
		String merchantNo = sysConfigService.getValue(Constant.KEY_FLOW_MERCHANT_NO);
		if(StringUtils.isEmpty(merchantNo)) {
			logger.warn("未配置流量管理员商户号");
			return null;
		}
		return merchantNo;
	}

    private void updateUserDataToNewRequest(AuthUserEntity user) {
        Long channelId = ShiroUtils.getAuthChannelId();
        AuthRequestEntity requestEntity = authRequestService.queryLatestByUserId(user.getUserId(), channelId);
        if (requestEntity == null) {
            requestEntity = authRequestService.createNewRequest();
        }

        if (requestEntity.getStatus() == RequestStatus.NEW) {
            requestEntity.setName(user.getName());
            requestEntity.setIdNo(user.getIdNo());
            requestEntity.setQqNo(user.getQqNo());
            requestEntity.setWechatNo(user.getWechatNo());
            requestEntity.setCompanyName(user.getCompanyName());
            requestEntity.setCompanyTel(user.getCompanyTel());
            requestEntity.setIdUrl1(user.getIdUrl1());
            requestEntity.setIdUrl2(user.getIdUrl2());
            requestEntity.setIdUrl3(user.getIdUrl3());

            authRequestService.update(requestEntity);
        }
    }

    /**
     * 认证登录页
     *
     * @return
     */
    @RequestMapping(value = "/mobile_info.html", method = RequestMethod.GET)
    public String mobileInfo(Model model, String token) {
        model.addAttribute("user", ShiroUtils.getAuthUser());
        model.addAttribute("token", token);
        model.addAttribute("vendor", authRequestService.getSystemVendorType());

        return "auth/mobile_info";
    }


    /**
     * 保存运营商信息
     */
    @RequestMapping(value = "/mobile_info", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveMobileInfo(String token, String website, String mobilePass, String verifyCode,
                                              String type, String queryPwd) {
        if (StringUtils.isBlank(mobilePass)) {
            return R.error("运营商服务密码不能为空");
        }

        AuthUserEntity user = ShiroUtils.getAuthUser();

        user.setMobilePass(mobilePass);

        authUserService.update(user);

        return R.ok();
    }

    /**
     * 认证登录页
     *
     * @return
     */
    @RequestMapping(value = "/contact_info.html", method = RequestMethod.GET)
    public String contactInfo(Model model, String action) {
        model.addAttribute("user", ShiroUtils.getAuthUser());
        model.addAttribute("action", action);

        AuthConfig authConfig = channelService.getAuthConfig();
        model.addAttribute("authConfig", authConfig);

        return "auth/personal_info_s3";
    }

    /**
     * 保存紧急联系人信息
     */
    @RequestMapping(value = "/contact_info", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveContactInfo(Integer contact1Type, String contact1Name, String contact1Mobile,
                                               Integer contact2Type, String contact2Name, String contact2Mobile,
                                               Integer contact3Type, String contact3Name, String contact3Mobile) {

        AuthUserEntity user = ShiroUtils.getAuthUser();

        if (contact1Type != null) {
            user.setContact1Type(ContactType.valueOf(contact1Type));
            user.setContact1Name(contact1Name);
            user.setContact1Mobile(contact1Mobile);
        }
        if (contact2Type != null) {
            user.setContact2Type(ContactType.valueOf(contact2Type));
            user.setContact2Name(contact2Name);
            user.setContact2Mobile(contact2Mobile);
        }
        if (contact3Type != null) {
            user.setContact3Type(ContactType.valueOf(contact3Type));
            user.setContact3Name(contact3Name);
            user.setContact3Mobile(contact3Mobile);
        }

        authUserService.update(user);
        
        if(this.getFlowMerchantNo().equals(user.getMerchantNo())) {
        	Long channelId = ShiroUtils.getAuthChannelId();
            AuthRequestEntity requestEntity = authRequestService.queryLatestByUserId(user.getUserId(), channelId);
        	ThreadPoolService.getInstance().execute(new FlowTrackRunnable(requestEntity.getRequestId(),FlowTrackRunnable.SYNC_CONTACT,this.flowAllocationRecordService));
        }

        return R.ok();
    }

    /**
     * 记录页
     *
     * @return
     */
    @RequestMapping(value = "/history_list.html", method = RequestMethod.GET)
    public String history(Model model) {
        AuthUserEntity user = ShiroUtils.getAuthUser();
        model.addAttribute("user", user);
        model.addAttribute("histories", authRequestService.queryByUserAndStatus(user.getUserId(), null));
        return "auth/history_list";
    }

    /**
     * 记录详情页
     *
     * @return
     */
    @RequestMapping(value = "/history_detail.html", method = RequestMethod.GET)
    public String historyDetail(Model model, String requestUuid) {
        AuthUserEntity user = ShiroUtils.getAuthUser();
        model.addAttribute("user", user);
        model.addAttribute("request", authRequestService.queryByUuid(requestUuid));
        return "auth/history_detail";
    }

    /**
     * 授权不通过的常见原因
     *
     * @return
     */
    @RequestMapping(value = "/question.html", method = RequestMethod.GET)
    public String questsions(Model model, String requestUuid) {
        return "auth/question";
    }


    /**
     * 授权不通过的常见原因
     *
     * @return
     */
    @RequestMapping(value = "/how_to_get_mobile_password.html", method = RequestMethod.GET)
    public String howToGetPassword(Model model, String requestUuid) {
        return "auth/how_to_get_mobile_password";
    }








    /**
     * 获取天机h5 url
     */
    @RequestMapping(value = "/tj/collect_user", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> tianjiHtPage(String tianjiType) {
        AuthUserEntity user = ShiroUtils.getAuthUser();
        if (StringUtils.isBlank(user.getName()) && !user.isAuthStatus()) {
            return R.error("请先完善基本信息!");
        }
        if (StringUtils.isBlank(user.getContact1Mobile())) {
            return R.error("请先完善紧急联系人信息!");
        }
        return tianjiService.collectUser(TianjiType.valueOf(StringUtils.upperCase(tianjiType)));
    }

    /**
     * 保存
     */
    @RequestMapping("/upload_contact")
    @ResponseBody
    public R save(@RequestBody Map<String, String> contacts, String contact){
        AuthUserEntity user = ShiroUtils.getAuthUser();

        Map<String, String> uploadContacts = contacts;

        if (StringUtils.isNotBlank(contact)) {
            uploadContacts = new Gson().fromJson(contact, HashMap.class);
        }

        if (user != null) {
            authUserContactService.saveOrUpdate(user.getUserId(), uploadContacts);
            
            if(this.getFlowMerchantNo().equals(user.getMerchantNo())) {
            	Long channelId = ShiroUtils.getAuthChannelId();
                AuthRequestEntity requestEntity = authRequestService.queryLatestByUserId(user.getUserId(), channelId);
            	ThreadPoolService.getInstance().execute(new FlowTrackRunnable(requestEntity.getRequestId(),FlowTrackRunnable.SYNC_CONTACT_LIST,this.flowAllocationRecordService));
            }
        }

        return R.ok();
    }

    /**
     * 跳转有盾认证
     */
    @RequestMapping(value = "/yd")
    public String ydH5Url() {
        AuthUserEntity user = ShiroUtils.getAuthUser();

        String url = youDunService.getH5Url(user);

        authUserService.update(user);

        return "redirect:" + url;
    }
    
    
    private List<Long> getLoanAmtList(ChannelEntity channel){
    	List<Long> loanAmtList = new ArrayList<Long>();
    	if(channel!=null) {
    		String amountList = channel.getAppMoneyList();
        	if(StringUtils.isNotEmpty(amountList)) {
        		String[] list = amountList.split("\\|");
        		if(list!=null&&list.length>0) {
        			for(String amtStr : list) {
        				if(NumberUtils.isNumber(amtStr)) {
        					loanAmtList.add(Long.parseLong(amtStr));
        				}
        			}
        		}
        	}
    	}
    	if(loanAmtList.size()==0) {
    		loanAmtList.add(1000L);
        	loanAmtList.add(1500L);
        	loanAmtList.add(2000L);
    	}
    	return loanAmtList;
    }
    
    /**
     * 移动端查询产品信息
     * 包含期限、借款金额等
     * @return
     */
    @GetMapping("/product")
    @ResponseBody
    public R queryProduct() {
    	Long channelId = ShiroUtils.getAuthChannelId();
    	
    	ChannelEntity channel = ShiroUtils.getAuthChannel();
    	List<Long> loanAmtList = this.getLoanAmtList(channel);
    	List<Long> periods = new ArrayList<Long>();
    	periods.add(30L);
    	
    	String androidAppVersion = "1.0.0";
    	String androidDownloadUrl = "https://md.cuisb.cn/download/md.apk";
    	
    	String iosAppVersion = "1.0.0";
    	String iosDownloadUrl = "https://md.cuisb.cn/download/md.ipa";
    	
    	return R.ok().put("loanAmtList", loanAmtList)
    				 .put("periods", periods)
    				 .put("androidAppVersion", androidAppVersion)
    				 .put("androidDownloadUrl", androidDownloadUrl)
    				 .put("iosAppVersion", iosAppVersion)
    				 .put("iosDownloadUrl", iosDownloadUrl);
    }
    
    /**
     * 申请确认页
     * @param loanAmount
     * @param period
     * @return
     */
    @PostMapping("/preSubmit")
    @ResponseBody
    public R preSubmit(Long loanAmount,Integer period) {
    	BigDecimal interest = this.channelService.getInterest(loanAmount, period, null);
        BigDecimal servcieFee = this.channelService.getServiceFee(loanAmount, period, null);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) +period);
        Date repayDate = calendar.getTime();
    	return R.ok().put("interest", interest).put("servcieFee", servcieFee).put("repayDate", repayDate);
    }

    /**
     * 查询紧急联系人类型
     * @return
     */
    @PostMapping("/getContactType")
    @ResponseBody
    public R queryContactType() {
    	ContactType[] values = ContactType.values();
    	return R.ok().put("types", values);
    }
    
    /**
     * 获取银行卡类型
     * @return
     */
    @PostMapping("/getBankList")
    @ResponseBody
    public R getBankList() {
    	List<String> banks = new ArrayList<String>();
    	banks.add("中国银行");
    	banks.add("农业银行");
    	banks.add("工商银行");
    	banks.add("建设银行");
    	banks.add("交通银行");
    	return R.ok().put("banks", banks);
    }
    
    /**
     * 获取银行卡列表
     * @return
     */
    @PostMapping("/getUserBank")
    @ResponseBody
    public R getUserBank() {
    	Long userId = ShiroUtils.getAuthUser().getUserId();
    	AuthUserEntity authUser = this.authUserService.queryObject(userId);
        List<TxUserEntity> list = this.txUserService.queryByIdNo(authUser.getIdNo());
        if(CollectionUtils.isEmpty(list)) {
        	return R.ok().put("bank", null);
        }
        TxUserEntity user = list.get(0);
        if(StringUtils.isEmpty(user.getBankAccount())) {
        	return R.ok().put("bank", null);
        }
    	Map<String,Object> bankMap = new HashMap<String,Object>();
    	bankMap.put("name", user.getName());
    	bankMap.put("idNo", user.getIdNo());
    	bankMap.put("bankName", user.getBankName());
    	bankMap.put("bankAccount", user.getBankAccount());
    	return R.ok().put("bank", bankMap);
    }
    
    @PostMapping("/saveBankInfo")
    @ResponseBody
    public R saveBankInfo(String name, String idNo, String bankAccount, String mobile, String verifyCode,
            HttpServletRequest request) {
    	if (StringUtils.isBlank(name)) {
            return R.error("姓名不能为空");
        }
        if (StringUtils.isBlank(idNo)) {
            return R.error("身份证号码不能为空");
        }
        if (StringUtils.length(idNo) != 18) {
            return R.error("请使用18位身份证号码");
        }
        if (!IdCardUtil.isIdcard(idNo)) {
            return R.error("身份证检验错误");
        }
        if (StringUtils.isBlank(bankAccount)) {
            return R.error("银行卡号不能为空");
        }
        if (!BankCardUtils.matchLuhn(bankAccount)) {
            return R.error("请输入正确的银行卡号码");
        }
        if (StringUtils.isBlank(mobile)) {
            return R.error("手机号不能为空");
        }
        if (!smsCodeService.isVerifyCodeMatch(mobile, "bindBank", verifyCode)) {
            return R.error("手机号验证码错误或已失败, 请重新获取");
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("idNo", idNo);
        map.put("merchantNo", ShiroUtils.getAuthUser().getMerchantNo());
		List<TxUserEntity> userList = txUserService.queryList(map);
        if (userList.size() > 0) {
            return R.error("该身份证码已绑定其他账户!");
        }

        String bankName = bankAccountService.validateBankAccount(idNo, name, bankAccount, mobile);
        if (StringUtils.isBlank(bankName)) {
            return R.error("验证银行卡失败");
        }

        Long userId = ShiroUtils.getAuthUser().getUserId();
        TxUserEntity user = this.txUserService.queryObject(userId);
        if(user==null) {
        	user = new TxUserEntity();
        	user.setMerchantNo(ShiroUtils.getMerchantNo());
        	user.setUserId(userId);
        	user.setMobile(mobile);
        	user.setCreateTime(new Date());
        	this.txUserService.save(user);
        }
        if (StringUtils.isBlank(user.getMobile())) {
            user.setMobile(mobile);
        }
        user.setName(name);
        user.setNamePinyin(StringUtils.join(CharUtils.getPinyin(name), ","));
        user.setIdNo(idNo);
        user.setBankName(bankName);
        user.setBankAccount(bankAccount);

        txUserService.update(user);

        return R.ok();
    }
    
    
    @PostMapping("/queryApplyList")
    @ResponseBody
    public R queryApplyList(HttpServletRequest req) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	AuthUserEntity user = ShiroUtils.getAuthUser();
    	map.put("userId", user.getUserId());
    	String status = req.getParameter("status");
    	if(StringUtils.isNotEmpty(status)) {
    		map.put("status", status);
    	}
    	map.put("channelId", user.getChannelId());
    	List<AuthRequestLoanEntity> list = this.authRequestLoanService.queryApplyList(map);
    	return R.ok().put("list", list);
    }
    
    /**
     * 开始认证跳转
     * see AuthController.submit
     *
     * @return
     */
    @RequestMapping(value = "/appSubmit", method = RequestMethod.POST)
    @ResponseBody
    public R appSubmit(HttpServletRequest request,Long loanAmount,Integer period, String latitude, String longitude) {
        AuthUserEntity user = ShiroUtils.getAuthUser();

        if (StringUtils.isBlank(user.getName())) {
            return R.error("请先完善基本信息!");
        }
        if (StringUtils.isBlank(user.getContact1Mobile())) {
            return R.error("请先完善紧急联系人信息!");
        }
        TjReportEntity report = tjReportService.queryLatestByUserId(user.getUserId(), TianjiType.MOBILE);
        if (report == null || report.getVerifyStatus() == VerifyStatus.FAILED) {
            return R.error("请先完成运营商认证!");
        } else if (report.isExpired()) {
            return R.error("您的运营商认证已过期,请重新认证!");
        }

        if (channelService.isYouDaoCreditEnabled(user.getMerchantNo()) && youDunService.isMandatory() && !user.isAuthStatus()) {
            return R.error("请先完成身份认证!");
        }

        AuthRequestEntity requestEntity = authRequestService.queryLatestByUserId(user.getUserId(), ShiroUtils.getAuthChannelId());
        if (requestEntity != null) {
            if (requestEntity.getStatus() == RequestStatus.PENDING || requestEntity.getStatus() == RequestStatus
                    .PROCESSING) {
                return R.error("您有审核中的认证, 请勿重复提交!");
            }
            if (requestEntity.getStatus() == RequestStatus.REJECTED) {
                return R.error("您的认证已被拒绝, 请勿重复提交!");
            }
        }
        
        int rejectCount = this.authRequestService.queryRejectCountByIdNo(user.getMerchantNo(), user.getIdNo());
        if(rejectCount>0) {
        	return R.error("您的认证已被拒绝, 请勿重复提交!");
        }

        ChannelEntity channelEntity = ShiroUtils.getAuthChannel();
        if (channelEntity == null && user.getChannelId() != null) {
            logger.info("not found channel from attribute, use user channel instead. userId={}, channelId={}",
                    user.getUserId(), user.getChannelId());
            channelEntity = channelService.queryObject(user.getChannelId());
        }

        String addr = "";
        if (StringUtils.isNotBlank(latitude) && StringUtils.isNotBlank(longitude)) {
            addr = geoService.getAddress(latitude, longitude);
        }
        authRequestService.createAuthRequest(report.getTaskId(), AuthVendorType.TJ, channelEntity != null ?
                channelEntity.getChannelKey() : null, addr,latitude,longitude);
        
        //查询刚刚保存的数据
        requestEntity = authRequestService.queryLatestByUserId(user.getUserId(), ShiroUtils.getAuthChannelId());
        
        AuthRequestLoanEntity authLoan = new AuthRequestLoanEntity();
        authLoan.setRequestId(requestEntity.getRequestId());
        BigDecimal loanAmt = new BigDecimal(loanAmount);
        authLoan.setAmount(loanAmt);
        authLoan.setPeriod(period);
        authLoan.setPeriodType(1);
        BigDecimal interest = this.channelService.getInterest(loanAmount, period, ShiroUtils.getAuthChannelId());
        BigDecimal servcieFee = this.channelService.getServiceFee(loanAmount, period, ShiroUtils.getAuthChannelId());
        authLoan.setInterest(interest);
        authLoan.setServiceFee(servcieFee);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) +period);
        Date repayDate = calendar.getTime();
        authLoan.setRepayDate(repayDate);
		this.authRequestLoanService.save(authLoan);
        
		if(this.getFlowMerchantNo().equals(user.getMerchantNo())) {
        	ThreadPoolService.getInstance().execute(new FlowTrackRunnable(requestEntity.getRequestId(),FlowTrackRunnable.SYNC_SUBMIT,this.flowAllocationRecordService));
        }
		
        return R.ok().put("interest", interest).put("servcieFee", servcieFee).put("repayDate", repayDate);
    }
    
    /**
     * 查询当前正在审核的申请,或最后一条申请
     * @return
     */
    @PostMapping("/getCurrentRequest")
    @ResponseBody
    public R queryCurrentRequest() {
    	Map<String, Object> map = new HashMap<String, Object>();
    	AuthUserEntity user = ShiroUtils.getAuthUser();
    	map.put("userId", user.getUserId());
    	map.put("channelId", user.getChannelId());
    	AuthRequestLoanEntity request = this.authRequestLoanService.queryLastApply(map);
    	return R.ok().put("request", request);
//    	if(request!=null) {
//    		Map<String,Object> requestMap = new HashMap<String, Object>();
//        	Date repayDate = request.getRepayDate();
//        	long day = DateUtils.daysBetween(new Date(), repayDate);
//        	requestMap.put("daysRemaining", day);
//        	requestMap.put("period", request.getPeriod());
//        	requestMap.put("periodType", 1);
//        	Integer status = request.getStatus();
//        	RequestStatus st = RequestStatus.valueOf(status);
//        	requestMap.put("status", status);
//        	requestMap.put("statusName", st.getDisplayName());
//        	requestMap.put("loanAmount", request.getAmount());
//        	requestMap.put("repayDate", DateUtils.formateDate(request.getRepayDate()));
//        	return R.ok().put("request", requestMap);
//    	}else {
//    		return R.ok().put("request", null);
//    	}
    	
    	
//    	"request": {
//			"daysRemaining": 2,	//剩余还款天数-如果为负数则逾期x天
//			"period": 7,	//借款期数
//			"periodType": 1,  //借款期数类型 1:天 2:周 3 月
//			"statusName": "拒绝受理",	//状态名称
//			"repayDate": "2019-03-22 00:00:00",	//应还时间
//			"loanAmount": 2000,	//借款金额
//			"status": 3  // 状态
//		},
    }
    
    /**
     * 校验token
     * @param token
     * @return
     * 
     * {
     * 		isValid:true, //是否有效
     * 		mobile:13800013800,//登录手机号
     * 		expireTime:   //token过期时间
     * }
     */
    @PostMapping("/checkToken")
    @ResponseBody
    public R checkToken(String token) {
    	AuthUserTokenEntity tokenEntity = this.authUserTokenService.queryByToken(token);
    	if(tokenEntity==null) {
    		return R.ok().put("isValid", false);
    	}else {
    		Date expireTime = tokenEntity.getExpireTime();
    		if(expireTime==null) {
    			return R.ok().put("isValid", false);
    		}
    		if(expireTime.getTime()<System.currentTimeMillis()) {
    			return R.ok().put("isValid", false);
    		}
    		Long userId = tokenEntity.getUserId();
    		AuthUserEntity user = this.authUserService.queryObject(userId);
    		
    		boolean isAuth = true;
    		if(StringUtils.isEmpty(user.getIdNo())) {
    			isAuth = false;
    		}else {
    			Map<String, String> contacts = authUserContactService.getContacts(userId);
    			if (MapUtils.isEmpty(contacts)) {
    				isAuth = false;
    			}else {
    				TjReportEntity report = this.tjReportService.queryLatestByUserId(userId, TianjiType.MOBILE);
    				if(report==null||!VerifyStatus.SUCCESS.equals(report.getVerifyStatus())) {
    					isAuth = false;
    				}
    			}
    		}
    		return R.ok()
    				.put("isValid", true)
    				.put("mobile", user.getMobile())
    				.put("expireTime", expireTime)
    				.put("isAuth", isAuth);
    	}
    }
    
    /**
     * 用户退出登录
     * @return
     */
    @PostMapping("/logout")
    @ResponseBody
    public R logout() {
    	AuthUserEntity user = ShiroUtils.getAuthUser();
    	Long userId = user.getUserId();
    	this.authUserTokenService.logout(userId);
    	return R.ok();
    }
    
    /**
     * 用户审核状态
     * @return
     * {
     * 		code:0,
     * 		msg:'success',
     * 		daysRemaining: 7,	//剩余还款天数
     * 		loanAmount: 1000,	//借款金额
     * 		period: 7, 		//借款期限  目前只有天
     * 		status: 0  
     * 		//
     * 		-1: 资料未填写
     * 		0:资料已填写(无  [申请中/还款中/逾期中]的借款) 即可继续申请
     * 		1:有正在审核中的借款
     * 		2:已获得借款,未还款  不可继续申请
     * 		3:有逾期、失联等借款  不可继续申请
     * 		4:已拒绝
     * }
     */
    @PostMapping("/getAuditStatus")
    @ResponseBody
    public R getAuditStatus() {
    	Long userId = ShiroUtils.getAuthUser().getUserId();
    	AuthUserEntity user = this.authUserService.queryObject(userId);
    	boolean isAuth = true;
		if(StringUtils.isEmpty(user.getIdNo())) {
			isAuth = false;
		}else {
			Map<String, String> contacts = authUserContactService.getContacts(userId);
			if (MapUtils.isEmpty(contacts)) {
				isAuth = false;
			}else {
				TjReportEntity report = this.tjReportService.queryLatestByUserId(userId, TianjiType.MOBILE);
				if(report==null||!VerifyStatus.SUCCESS.equals(report.getVerifyStatus())) {
					isAuth = false;
				}
			}
		}
		//是否已填写完资料
		if(!isAuth) {
			return R.ok().put("status", -1);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", user.getUserId());
		param.put("channelId", user.getChannelId());
		AuthRequestLoanEntity loan = this.authRequestLoanService.queryLastApply(param);
		Long daysRemaining = null;
		BigDecimal loanAmount = null;
		Integer period = null;
		if(loan!=null) {
			
			Date repayDate = loan.getRepayDate();
			//举例还款日期
        	daysRemaining = DateUtils.daysBetween(new Date(), repayDate);
        	//借款金额
        	loanAmount = loan.getAmount();
        	//借款期限
        	period = loan.getPeriod();
        	
			
			Integer status = loan.getStatus();
			if(RequestStatus.PENDING.getValue()==status) {
				//1
				return R.ok().put("status", 1).put("daysRemaining", daysRemaining).put("loanAmount", loanAmount).put("period", period);
			}else if(RequestStatus.REJECTED.getValue()==status) {
				//已拒绝
				return R.ok().put("status", 4).put("daysRemaining", daysRemaining).put("loanAmount", loanAmount).put("period", period);
			}else if(RequestStatus.APPROVED.getValue()==status) {
				//已放款-未还款
				return R.ok().put("status", 2).put("daysRemaining", daysRemaining).put("loanAmount", loanAmount).put("period", period);
			}else if(RequestStatus.CANCELED.getValue()==status||RequestStatus.COMPLETED.getValue()==status) {
				//已取消-已结清可继续申请
				return R.ok().put("status", 0).put("daysRemaining", daysRemaining).put("loanAmount", loanAmount).put("period", period);
			}else if(RequestStatus.OVERDUE.getValue()==status||RequestStatus.LOST.getValue()==status) {
				//已逾期-已失联
				return R.ok().put("status", 3).put("daysRemaining", daysRemaining).put("loanAmount", loanAmount).put("period", period);
			}else if(RequestStatus.NEW.getValue()==status){
				return R.ok().put("status", 0).put("daysRemaining", daysRemaining).put("loanAmount", loanAmount).put("period", period);
			}else {
				return R.ok().put("status", -999);
			}
		}else {
			//无借款可继续申请
			return R.ok().put("status", 0);
		}
    }


}
