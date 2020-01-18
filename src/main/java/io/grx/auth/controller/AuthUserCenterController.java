package io.grx.auth.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import io.grx.auth.entity.requestModel.RequestBindBankModel;
import io.grx.auth.entity.requestModel.RequestContactNameListModel;
import io.grx.auth.entity.requestModel.RequestContactNameModel;
import io.grx.auth.entity.requestModel.RequestInfoModel;
import io.grx.auth.thread.FlowTrackRunnable;
import io.grx.common.utils.*;
import io.grx.modules.auth.entity.*;
import io.grx.modules.auth.enums.ContactType;
import io.grx.modules.auth.enums.RequestStatus;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.auth.service.*;
import io.grx.modules.flow.service.FlowAllocationRecordService;
import io.grx.modules.opt.entity.ChannelEntity;
import io.grx.modules.opt.service.ChannelService;
import io.grx.modules.sys.service.SysConfigService;
import io.grx.tianji.TianjiUtils;
import io.grx.youdun.YouDunUtils;
import io.grx.youdun.model.BankQueryBody;
import io.grx.youdun.model.BankQueryModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/auth/usercenter/")
public class AuthUserCenterController {

    public  final String AUTH_TOKEN = "token";

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private AuthUserOCRService authUserOCRService;

    @Autowired
    private AuthUserOCRRequestLogService authUserOCRRequestLogService;

    @Autowired
    private AuthUserBankCardService authUserBankCardService;

    @Autowired
    private AuthUserTianJiService authUserReportTJService;

    @Autowired
    private AuthUserTokenService authUserTokenService;

    @Autowired
    private AuthRequestService authRequestService;

    @Autowired
    private AuthRequestLoanService authRequestLoanService;
    @Autowired
    private ChannelService channelService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private FlowAllocationRecordService flowAllocationRecordService;

    @Autowired
    private AuthUserContactService authUserContactService;

    @Value("${youdun.ocrCallBackUrl}")
    private String ocrCallBackUrl;

    @Value("${tianji.callBackDomain}")
    private String callBackDomain;


    @RequestMapping(value = "/idCard",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> idCard(HttpServletRequest request) {

        long userId = getUserId(request);
        AuthUserEntity user  = authUserService.queryObject(userId);

        if(user.isAuthStatus()){

            AuthUserOCREntity authUserOCREntity = authUserOCRService.queryByUserId(user.getUserId());

            Map<String,Object> map = new HashMap<>();
            map.put("name",user.getName());
            map.put("idNo",user.getIdNo());
            map.put("idUrl1",authUserOCREntity.getIdUrl1());
            map.put("idUrl2",authUserOCREntity.getIdUrl2());

            return R.ok().put("data",map);
        }else{

            //没有认证需跳转认证页面
            String orderId = "OCR-"+ DateUtils.getStringDate();

            //保存请求记录
            AuthUserOCRRequestLogEntity authUserOCRRequestLogEntity = new AuthUserOCRRequestLogEntity();
            authUserOCRRequestLogEntity.setUserId(user.getUserId());
            authUserOCRRequestLogEntity.setOrderId(orderId);
            authUserOCRRequestLogEntity.setStatus(0);
            authUserOCRRequestLogEntity.setCreateTime(DateUtils.formateDateTime(new Date()));
            authUserOCRRequestLogService.save(authUserOCRRequestLogEntity);

            String url = YouDunUtils.getOCRUrl(orderId,ocrCallBackUrl);
            return R.error(400,"跳转认证页面").put("url",url);
        }
    }


    /**
     * 保存通讯录
     */
    @RequestMapping(value = "/uploadContact" ,method = RequestMethod.POST)
    @ResponseBody
    public R uploadContact(HttpServletRequest request, @RequestBody RequestContactNameListModel model){


        long userId = getUserId(request);
        AuthUserEntity user  = authUserService.queryObject(userId);

        if(user.isAuthStatus()){

            if(authUserContactService.queryObject(user.getUserId())==null){
                AuthUserContactEntity newContact = new AuthUserContactEntity();
                newContact.setUserId(user.getUserId());
                newContact.setContact(model.getStr());
                authUserContactService.save(newContact);
            }
        }

        return R.ok();
    }


    @RequestMapping(value = "/operator", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> operator(HttpServletRequest request) {

        long userId = getUserId(request);
        AuthUserEntity user  = authUserService.queryObject(userId);

        AuthUserStep  authUserStep =  validate(user,2-1);

        if(authUserStep.isStatus()){

            if(!user.isAuthOperatorStatus()) {

                AuthUserTianJi  authUserTianJi =  authUserReportTJService.queryByUserId(user.getUserId());
                String outUniqueId = user.getUserId() + "-" + DateUtils.getStringDate();
                String str = TianjiUtils.getTianjiUrl(user.getName(), user.getIdNo(), user.getMobile(), user.getUserId(), outUniqueId,callBackDomain);
                if (!"".equals(str)) {

                    if(authUserTianJi == null ){
                        AuthUserTianJi authUserReportTJ = new AuthUserTianJi();
                        authUserReportTJ.setUserId(user.getUserId());
                        authUserReportTJ.setOutUniqueId(outUniqueId);
                        authUserReportTJ.setSearchId("");
                        authUserReportTJ.setDataStatus(0);
                        authUserReportTJ.setDetailStatus(0);
                        authUserReportTJ.setCreateTime(DateUtils.formateDateTime(new Date()));
                        authUserReportTJ.setDataStr("");
                        authUserReportTJService.save(authUserReportTJ);
                    }else{


                            long l1 = DateUtils.parseDateTime(authUserTianJi.getCreateTime()).getTime();
                            long l2 = new Date().getTime();

                            long t = 3*60*1000;

                            if((l2-l1)>t ){

                                AuthUserTianJi authUserReportTJ = new AuthUserTianJi();
                                authUserReportTJ.setOutUniqueId(outUniqueId);
                                authUserReportTJ.setUserId(user.getUserId());
                                authUserReportTJ.setCreateTime(DateUtils.formateDateTime(new Date()));
                                authUserReportTJService.update(authUserReportTJ);
                            }else{
                                return R.error(500,"请等待处理结果或稍后认证！ ("+Math.abs(l2-l1-t)/60/100+")");
                            }
                    }

                    return R.error(400,"跳转认证页面").put("url",str);
                }else{
                    return R.error();
                }
            }else{
                //已认证
                return R.ok();
            }
        }else{
            return R.error(authUserStep.getCode(),authUserStep.getMsg());
        }
    }

    /**
     * 银行卡
     * @return
     */
    @RequestMapping(value = "/bankCard", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> bankCard(HttpServletRequest request) {

        long userId = getUserId(request);
        AuthUserEntity user  = authUserService.queryObject(userId);

        AuthUserStep authUserStep = validate(user,3-1);
        if(authUserStep.isStatus()){

            Map<String,Object> map = new HashMap<>();
            map.put("name",user.getName());

            AuthUserBankCard authUserBankCard = authUserBankCardService.queryByUserId(user.getUserId());
            if(authUserBankCard!=null){
                map.put("bankCardName",authUserBankCard.getBankCardName());
                map.put("bankCardNo",authUserBankCard.getBankCardNo());
                map.put("mobile",authUserBankCard.getMobile());
                map.put("status",1);
            }else{
                map.put("status",0);
            }
            return R.ok().put("data",map);

        }else{
            return R.error(authUserStep.getCode(),authUserStep.getMsg());
        }
    }


    /**
     * 绑定银行卡
     * @return
     */
    @RequestMapping(value = "/bindBankCard", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> bindBankCard(HttpServletRequest request,@RequestBody RequestBindBankModel model) {

        long userId = getUserId(request);
        AuthUserEntity user  = authUserService.queryObject(userId);
        if(user==null){
            return R.error(600,"请重新登录");
        }

        AuthUserStep authUserStep = validate(user,3-1);
        if(authUserStep.isStatus()) {

            if(authUserBankCardService.queryByUserId(user.getUserId())!=null){
                return R.error("您已经银行卡认证了");
            }

            if (StringUtils.isBlank(model.getBankCardNo())) {
                return R.error("银行卡号不能为空");
            }
            if (StringUtils.isBlank(model.getMobile())) {
                return R.error("预留手机号不能为空");
            }

            String str = YouDunUtils.BindBankCard(user.getName(),user.getIdNo(),model.getBankCardNo(),model.getMobile());

            BankQueryModel bankQueryModel =  JSONObject.parseObject(str, BankQueryModel.class);
            if(bankQueryModel.getBody()!=null){

                BankQueryBody bankQueryBody = bankQueryModel.getBody();

                if("1".equals(bankQueryBody.getStatus())){

                    //保存数据
                    AuthUserBankCard authUserBankCard = new AuthUserBankCard();
                    authUserBankCard.setUserId(user.getUserId());
                    authUserBankCard.setBankCardNo(model.getBankCardNo());
                    authUserBankCard.setMobile(model.getMobile());
                    authUserBankCard.setBankCardName(bankQueryBody.getBank_name());
                    authUserBankCard.setBankOrgName(bankQueryBody.getOrg_name());
                    authUserBankCard.setCreateTime(DateUtils.formateDateTime(new Date()));
                    authUserBankCardService.save(authUserBankCard);

                    return R.ok("验证成功");
                }else{
                    return R.error(bankQueryBody.getMessage());
                }
            }else{
                return R.error("请求出错");
            }

        }else{
            return R.error(authUserStep.getCode(),authUserStep.getMsg());
        }
    }


    @RequestMapping(value = "/getContactName", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getContactName(HttpServletRequest request) {

        long userId = getUserId(request);
        AuthUserEntity user = authUserService.queryObject(userId);
        if(user==null){
            return R.error(600,"请重新登录");
        }

        AuthUserStep authUserStep =  validate(user,4-1);
        if(authUserStep.isStatus()) {

            String str = "";
            if(StringUtils.isNotBlank(user.getContact1Name())){


                str = user.getContact1Type().getValue()+"#"+user.getContact1Name()+"#"+user.getContact1Mobile()+",";

                str += user.getContact2Type().getValue()+"#"+user.getContact2Name()+"#"+user.getContact2Mobile()+",";

            }

            return R.ok().put("str",str);

        }else{
            return R.error(authUserStep.getCode(),authUserStep.getMsg());
        }
    }


    /**
     * 保存紧急联系人
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/saveContactName", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveContactName(HttpServletRequest request,@RequestBody RequestContactNameModel model) {

        long userId = getUserId(request);
        AuthUserEntity user = authUserService.queryObject(userId);
        if(user==null){
            return R.error(600,"请重新登录");
        }

        AuthUserStep authUserStep =  validate(user,4-1);
        if(authUserStep.isStatus()) {

            if("".equals(model.getC1())){

                return R.error("请输入紧急联系人1");
            }

            if("".equals(model.getC2())){

                return R.error("请输入紧急联系人2");
            }

            System.out.println(model.toString());

            String [] c1 = model.getC1().split("#");
            String [] c2 = model.getC2().split("#");

            user.setContact1Type(ContactType.valueOf(Integer.valueOf(c1[0])));
            user.setContact1Name(c1[1]);
            user.setContact1Mobile(c1[2]);

            user.setContact2Type(ContactType.valueOf(Integer.valueOf(c2[0])));
            user.setContact2Name(c2[1]);
            user.setContact2Mobile(c2[2]);

            authUserService.update(user);

            return R.ok();

        }else{
            return R.error(authUserStep.getCode(),authUserStep.getMsg());
        }
    }



    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> info(HttpServletRequest request) {

        long userId = getUserId(request);
        AuthUserEntity user  = authUserService.queryObject(userId);
        if(user!=null) {
            AuthUserInfoEntity authUserInfoEntity = new AuthUserInfoEntity();
            authUserInfoEntity.setCompanyAddr(user.getCompanyAddr());
            authUserInfoEntity.setCompanyJob(user.getCompanyJob());
            authUserInfoEntity.setCompanyName(user.getCompanyName());
            authUserInfoEntity.setCompanyTel(user.getCompanyTel());
            authUserInfoEntity.setWechatNo(user.getWechatNo());
            authUserInfoEntity.setQqNo(user.getQqNo());
            authUserInfoEntity.setSalary(user.getSalary());

            return R.ok().put("data", authUserInfoEntity);
        }else{
            return R.error(600,"请重新登录");
        }
    }

    /**
     * 保存个人信息
     * @param model
     * @return
     */
    @RequestMapping(value = "/saveinfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveinfo(HttpServletRequest request,@RequestBody RequestInfoModel model) {

        long userId = getUserId(request);
        AuthUserEntity user = authUserService.queryObject(userId);
        if(user==null){
            return R.error(600,"请重新登录");
        }

        if(StringUtils.isBlank(model.getWechatNo())){
            return R.error("请输入微信号");
        }
        if(StringUtils.isBlank(model.getQqNo())){
            return R.error("请输入QQ号");
        }
        /*
        if(StringUtils.isBlank(model.getEducation())){
            return R.error("请输入学历");
        }

        if(StringUtils.isBlank(model.getAddr())){
            return R.error("请输入居住地址");
        }
        */
        if(StringUtils.isBlank(model.getCompanyName())){
            return R.error("请输入公司名称");
        }

        if(StringUtils.isBlank(model.getCompanyTel())){
            return R.error("请输入公司联系方式");
        }

        if(StringUtils.isBlank(model.getSalary())){
            return R.error("请输入薪资");
        }

        if(StringUtils.isBlank(model.getCompanyJob())){
            return R.error("请输入公司职位");
        }

        if(StringUtils.isBlank(model.getCompanyAddr())){
            return R.error("请输入公司地址");
        }

        user.setWechatNo(model.getWechatNo());
        user.setQqNo(model.getQqNo());
        user.setCompanyName(model.getCompanyName());
        user.setCompanyTel(model.getCompanyTel());
        user.setCompanyJob(model.getCompanyJob());
        user.setCompanyAddr(model.getCompanyAddr());
        user.setSalary(model.getSalary());

        authUserService.update(user);

        return R.ok();
    }


    /**
     * 申请借款
     *
     * @return
     */
    @RequestMapping(value = "/borrowSubmit", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> borrowSubmit(HttpServletRequest request) {

        long userId = getUserId(request);
        AuthUserEntity user = authUserService.queryObject(userId);
        if(user==null){
            return R.error(600,"请重新登录");
        }
        AuthUserStep authUserStep = validate(user,3);
        if(!authUserStep.isStatus()){
            return R.error(authUserStep.getCode(),authUserStep.getMsg());
        }else {

            boolean flag = false;
            AuthRequestEntity requestEntity = authRequestService.queryLatestByUserId(user.getUserId(), user.getChannelId());
            if (requestEntity != null) {
                if (requestEntity.getStatus() == RequestStatus.PENDING || requestEntity.getStatus() == RequestStatus
                        .PROCESSING) {
                    return R.error("您有审核中的认证!");
                }
                if (requestEntity.getStatus() == RequestStatus.REJECTED) {
                    return R.error("您的认证已被拒绝!");
                }

                if (requestEntity.getStatus() == RequestStatus.APPROVED) {
                    return R.error("您有未还款的借款!");
                }

                if (requestEntity.getStatus() == RequestStatus.OVERDUE) {
                    return R.error("您有逾期的借款!");
                }

                if (requestEntity.getStatus() == RequestStatus.CANCELED) {
                    return R.error("您有已取消的借款!");
                }


                //完成所有认证后自动修改借款申请状态
                if (requestEntity.getStatus() == RequestStatus.NEW) {

                    AuthRequestEntity updateModel = new AuthRequestEntity();
                    updateModel.setMerchantNo(requestEntity.getMerchantNo());
                    updateModel.setRequestId(requestEntity.getRequestId());
                    updateModel.setStatus(RequestStatus.PENDING);
                    updateModel.setVerifyStatus(VerifyStatus.SUBMITTED);
                    updateModel.setMerchantNo(user.getMerchantNo());
                    authRequestService.update(updateModel);
                }
                //如果是已结清或者已取消的，可以再申请借款
                if ((requestEntity.getStatus() == RequestStatus.COMPLETED) || (requestEntity.getStatus() == RequestStatus.CANCELED)) {

                    flag = true;
                }
            } else {
                flag = true;
            }

            if (flag) {

                ChannelEntity channelEntity = channelService.queryObject(user.getChannelId());

                AuthRequestEntity authRequestEntitySaveModel = new AuthRequestEntity();

                authRequestEntitySaveModel.setRequestId(null);
                authRequestEntitySaveModel.setMerchantNo(user.getMerchantNo());
                authRequestEntitySaveModel.setChannelId(user.getChannelId());
                authRequestEntitySaveModel.setRequestUuid(UUIDGenerator.getShortUUID());
                authRequestEntitySaveModel.setUserId(user.getUserId());
                authRequestEntitySaveModel.setDeptId(channelEntity.getDeptId());
                authRequestEntitySaveModel.setCreateTime(new Date());
                authRequestEntitySaveModel.setAssigneeId(channelService.getAssigneeId(channelEntity));
                authRequestEntitySaveModel.setProcessorId(null);
                authRequestEntitySaveModel.setName(user.getName());
                authRequestEntitySaveModel.setIdNo(user.getIdNo());
                authRequestEntitySaveModel.setQqNo(user.getQqNo());
                authRequestEntitySaveModel.setContact1Type(user.getContact1Type());
                authRequestEntitySaveModel.setContact1Name(user.getContact1Name());
                authRequestEntitySaveModel.setContact1Mobile(user.getContact1Mobile());
                authRequestEntitySaveModel.setContact2Type(user.getContact2Type());
                authRequestEntitySaveModel.setContact2Name(user.getContact2Name());
                authRequestEntitySaveModel.setContact2Mobile(user.getContact2Mobile());
                authRequestEntitySaveModel.setIdUrl1(user.getIdUrl1());
                authRequestEntitySaveModel.setIdUrl2(user.getIdUrl2());
                authRequestEntitySaveModel.setIdUrl3(user.getIdUrl3());
                authRequestEntitySaveModel.setWechatNo(user.getWechatNo());
                authRequestEntitySaveModel.setCompanyName(user.getCompanyName());
                authRequestEntitySaveModel.setCompanyAddr(user.getCompanyAddr());
                authRequestEntitySaveModel.setCompanyTel(user.getCompanyTel());
                authRequestEntitySaveModel.setVerifyStatus(VerifyStatus.SUBMITTED);
                authRequestEntitySaveModel.setStatus(RequestStatus.PENDING);
                //经度+纬度

                this.authRequestService.save(authRequestEntitySaveModel);

                requestEntity = authRequestService.queryLatestByUserId(user.getUserId(), user.getChannelId());

            }

            long loanAmount = 20000L;
            int period = 7;

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
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + period);
            Date repayDate = calendar.getTime();
            authLoan.setRepayDate(repayDate);
            this.authRequestLoanService.save(authLoan);

            if (this.getFlowMerchantNo().equals(user.getMerchantNo())) {
                ThreadPoolService.getInstance().execute(new FlowTrackRunnable(requestEntity.getRequestId(), FlowTrackRunnable.SYNC_SUBMIT, this.flowAllocationRecordService));
            }

        }
        return R.ok();
    }

    /**
     * 借款记录
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryApplyList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> queryApplyList(HttpServletRequest request) {

        long userId = getUserId(request);
        Map<String, Object> map = new HashMap<String, Object>();
        AuthUserEntity user = authUserService.queryObject(userId);
        map.put("userId", user.getUserId());
        map.put("channelId", user.getChannelId());
        List<AuthRequestLoanEntity> list = authRequestLoanService.queryApplyList(map);
        return R.ok().put("list", list);
    }


    public long getUserId(HttpServletRequest request){

        try
        {
            String token = request.getHeader(AUTH_TOKEN);
            if (StringUtils.isNotBlank(token)) {
                return authUserTokenService.getUserIdByToken(token);
            }
        }catch (Exception ex){

        }
        return 0;
    }

    private String getFlowMerchantNo() {
        String merchantNo = sysConfigService.getValue(Constant.KEY_FLOW_MERCHANT_NO);
        if(StringUtils.isEmpty(merchantNo)) {
           // logger.warn("未配置流量管理员商户号");
            return null;
        }
        return merchantNo;
    }


    /**
     * 验证步骤
     * 1，先实名
     * 2，运营商
     * 3，绑卡
     * 4，保存紧急联系人
     * @param user
     * @param step
     * @return
     */
    public AuthUserStep validate(AuthUserEntity user,int step){

        AuthUserStep model = new AuthUserStep();
        model.setStep(step);

        if(user == null){
            model.setCode(600);
            model.setMsg("请重新登录");
            return model;
        }

         if(step == 1){

             if(user.isAuthStatus()){
                 model.setStatus(true);
             }else{
                 model.setMsg("请先完成实名认证");
                 model.setCode(-1);
             }

         }else if(step == 2){

             if(user.isAuthStatus()){

                 if(user.isAuthOperatorStatus()){
                     model.setStatus(true);
                 }else{
                     model.setMsg("请先完成运营商认证");
                     model.setCode(-2);
                 }
             }else{
                 model.setMsg("请先完成实名认证");
                 model.setCode(-1);
             }

         }else if(step == 3){

             if(user.isAuthStatus()){
                 if(user.isAuthOperatorStatus()){
                     if(authUserBankCardService.queryByUserId(user.getUserId())!=null){
                         model.setStatus(true);
                     }else{
                         model.setMsg("请先绑定银行卡");
                         model.setCode(-3);
                     }
                 }else{
                     model.setMsg("请先完成运营商认证");
                     model.setCode(-2);
                 }
             }else{
                 model.setMsg("请先完成实名认证");
                 model.setCode(-1);
             }
         }else if(step == 4){
             if(user.isAuthStatus()){
                 if(user.isAuthOperatorStatus()){
                     if(authUserBankCardService.queryByUserId(user.getUserId())!=null){
                         if(StringUtils.isNotBlank(user.getContact1Name())){
                             model.setStatus(true);
                         }else{
                             model.setMsg("请填写紧急联系人");
                             model.setCode(-4);
                         }
                     }else{
                         model.setMsg("请先绑定银行卡");
                         model.setCode(-3);
                     }
                 }else{
                     model.setMsg("请先完成运营商认证");
                     model.setCode(-2);
                 }
             }else{
                 model.setMsg("请先完成实名认证");
                 model.setCode(-1);
             }
         }

         return model;

    }




}
