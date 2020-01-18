package io.grx.wx.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.grx.common.service.BankAccountService;
import io.grx.common.service.SmsCodeService;
import io.grx.common.service.impl.LuosimaoService;
import io.grx.common.utils.BankCardUtils;
import io.grx.common.utils.CharUtils;
import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.IdCardUtil;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxUserPasswordService;
import io.grx.modules.tx.service.TxUserService;
import io.grx.modules.tx.service.TxUserTokenService;
import io.jsonwebtoken.lang.Assert;

@Controller
@RequestMapping("/rcpt")
public class RegistrationController extends BaseController {
    @Autowired
    private TxUserService txUserService;

    @Autowired
    private TxUserPasswordService passwordService;

    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private SmsCodeService smsCodeService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private TxUserTokenService txUserTokenService;

    @Autowired
    private LuosimaoService luosimaoService;

    private static final String CODE_TYPE_BIND_BANK = "bindBank";
    private static final String CODE_TYPE_CHANGE_PASS = "password";
//
//    private Cache<String, String> mobileCache = CacheBuilder.newBuilder().
//            expireAfterWrite(1, TimeUnit.MINUTES).
//            build();
//
//    private Cache<String, String> bindBankVerifyCodeCache = CacheBuilder.newBuilder().
//            expireAfterWrite(5, TimeUnit.MINUTES).
//            build();
//
//    private Cache<String, String> changePasswordVerifyCodeCache = CacheBuilder.newBuilder().
//            expireAfterWrite(5, TimeUnit.MINUTES).
//            build();
//
//    private Cache<String, String> verifyCodeCache = CacheBuilder.newBuilder().
//            expireAfterWrite(5, TimeUnit.MINUTES).
//            build();

    /**
     * 绑定手机页面
     *
     * @return
     */
    @RequestMapping(value = "/bind_mobile.html", method = RequestMethod.GET)
    public String bindMobilePate(Model model, HttpServletRequest request) {
        TxUserEntity user = getUser(request);

//        if (CharUtils.isNotBlank(user.getMobile())) {
//            return "redirect:main";
//        }
        return TX_TEMPLATE_PATH + "bind_mobile";
    }

    /**
     * 绑定手机
     */
    @RequestMapping(value = "/bind_mobile", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> bindMobile(String mobile, String code, HttpServletRequest request) {
//        System.out.println("mobile in request: " + request.getParameter("mobile"));
        if (StringUtils.isBlank(mobile)) {
            return R.error("手机号不能为空");
        }

        if (StringUtils.isBlank(code) || !StringUtils.equalsIgnoreCase(code, "123456")) {
            return R.error("手机号验证码不正确");
        }

        TxUserEntity user = getUser(request);
        user.setMobile(mobile);

        txUserService.update(user);

        return R.ok();
    }

    /**
     * 绑定手机页面
     *
     * @return
     */
    @RequestMapping(value = "/bind_bank.html", method = RequestMethod.GET)
    public String bindBankPage(Model model, HttpServletRequest request, String returnUrl, String txUuid) {
        TxUserEntity user = getUser(request);

        if (StringUtils.isNotBlank(user.getName())) {
            return "redirect:/rcpt/index.html";
        }

        model.addAttribute("needCaptcha", luosimaoService.needCaptcha());
        model.addAttribute("captchaKey", luosimaoService.getSiteKey());

        if (txUuid != null) {
            TxBaseEntity baseEntity = txBaseService.queryByUuid(txUuid);
            if (baseEntity != null) {
                if (baseEntity.getBorrowerUserId() == null) {
                    model.addAttribute("txUserName", baseEntity.getBorrowerName());
                } else if (baseEntity.getLenderUserId() == null) {
                    model.addAttribute("txUserName", baseEntity.getLenderName());
                }
            }
        }
        model.addAttribute("returnUrl", StringUtils.defaultString(returnUrl));
        return TX_TEMPLATE_PATH + "bind_bank";
    }

    /**
     * 绑定手机
     */
    @RequestMapping(value = "/bind_bank", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> bindBank(String name, String idNo, String bankAccount, String mobile, String verifyCode,
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

        if (!smsCodeService.isVerifyCodeMatch(mobile, CODE_TYPE_BIND_BANK, verifyCode)) {
            return R.error("手机号验证码错误或已失败, 请重新获取");
        }

        List<TxUserEntity> userList = txUserService.queryByIdNo(idNo);
        if (userList.size() > 0) {
            return R.error("该身份证码已绑定其他账户!");
        }

        String bankName = bankAccountService.validateBankAccount(idNo, name, bankAccount, mobile);
        if (StringUtils.isBlank(bankName)) {
            return R.error("验证银行卡失败");
        }

        TxUserEntity user = getUser(request);
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

    /**
     * 获取更改密码的手机验证码
     */
//    @RequestMapping(value = "/get_bind_bank_code", method = RequestMethod.POST)
//    @ResponseBody
//    public Map<String, Object> getBindBankCode(String mobile) {
//        if (StringUtils.isBlank(mobile)) {
//            return R.error("手机号不能为空");
//        }
//
//        return smsCodeService.getVerifyCode(mobile, CODE_TYPE_BIND_BANK);
//    }

    /**
     * 设置交易密码页面
     *
     * @return
     */
    @RequestMapping(value = "/set_password.html", method = RequestMethod.GET)
    public String setPasswordPage(Model model, HttpServletRequest request, String returnUrl) {
        TxUserEntity user = getUser(request);

        model.addAttribute("returnUrl", StringUtils.defaultString(returnUrl));
        return TX_TEMPLATE_PATH + "set_password";
    }

    /**
     * 获取绑定银行卡的手机验证码
     */
    @RequestMapping(value = "/set_password", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> setPassword(HttpServletRequest request, String p) {
        Assert.notNull(p);

        TxUserEntity user = getUser(request);
        passwordService.saveOrUpdatePassword(user.getUserId(), p);

        return R.ok();
    }


    /**
     * 设置交易密码页面
     *
     * @return
     */
    @RequestMapping(value = "/verify_sms.html", method = RequestMethod.GET)
    public String verifySmsPage(Model model, HttpServletRequest request, String forwardUrl, String type) {
        TxUserEntity user = getUser(request);
        model.addAttribute("forwardUrl", forwardUrl);
        model.addAttribute("type", type);

        model.addAttribute("needCaptcha", luosimaoService.needCaptcha());
        model.addAttribute("captchaKey", luosimaoService.getSiteKey());

        return TX_TEMPLATE_PATH + "verify_sms_code";
    }


    /**
     * 获取手机验证码
     */
//    @RequestMapping(value = "/get_sms_code", method = RequestMethod.GET)
//    @ResponseBody
//    public Map<String, Object> getChangePasswordCode(final String type) {
//        TxUserEntity user = ShiroUtils.getTxUser();
//        if (StringUtils.isBlank(user.getMobile())) {
//            return R.error("用户没有绑定手机号");
//        }
//
//        return smsCodeService.getVerifyCode(user.getMobile(), type);
//    }

    /**
     * 验证手机验证码
     */
    @RequestMapping(value = "/verify_sms_code", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> verifyChangePasswordCode(String code, String type) {
        TxUserEntity user = ShiroUtils.getTxUser();

        if (!smsCodeService.isVerifyCodeMatch(user.getMobile(), type, code)) {
            return R.error("验证码错误, 请稍后重新尝试");
        }

        return R.ok();
    }

    /**
     * 更换手机页面
     *
     * @return
     */
    @RequestMapping(value = "/change_mobile.html", method = RequestMethod.GET)
    public String changeMobilePage(Model model) {

        model.addAttribute("needCaptcha", luosimaoService.needCaptcha());
        model.addAttribute("captchaKey", luosimaoService.getSiteKey());

        return TX_TEMPLATE_PATH + "change_mobile";
    }

    /**
     * 更换手机
     */
    @RequestMapping(value = "/change_mobile", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> changeMobile(String mobile, String verifyCode, String type) {
        TxUserEntity user = ShiroUtils.getTxUser();

        if (StringUtils.equals(mobile, user.getMobile())) {
            return R.error("新手机号不能与当前手机号一样");
        }

        if (!smsCodeService.isVerifyCodeMatch(mobile, type, verifyCode)) {
            return R.error("验证码错误, 请稍后重新尝试");
        }

        if (txUserService.queryByMobile(mobile) != null) {
            return R.error("该手机号已绑定其他账号!");
        }

        user.setMobile(mobile);
        txUserService.update(user);

        return R.ok();
    }

    /**
     * 更换手机
     */
    @RequestMapping(value = "/unbind_wechat", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> unbindWechat() {
        TxUserEntity user = ShiroUtils.getTxUser();
        user.setWechatId(null);
        user.setNickName(null);
        user.setHeadImgUrl(null);
        txUserService.update(user);

        txUserTokenService.logout(user.getUserId());

        return R.ok().put("isWechat", HttpContextUtils.isWechatClient(
                HttpContextUtils.getHttpServletRequest()));
    }
}
