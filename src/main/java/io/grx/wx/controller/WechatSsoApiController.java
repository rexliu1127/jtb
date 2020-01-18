package io.grx.wx.controller;

import java.util.Date;
import java.util.Map;

import io.grx.modules.tx.entity.TxUserInviteEntity;
import io.grx.modules.tx.service.TxUserInviteService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.grx.common.service.SmsCodeService;
import io.grx.common.service.impl.LuosimaoService;
import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.R;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.service.TxUserService;
import io.grx.modules.tx.service.TxUserTokenService;
import io.grx.wx.utils.RestUtils;
import io.grx.wx.utils.WechatUtils;

@RestController
@RequestMapping("/rcpt")
public class WechatSsoApiController {

    private final Logger logger = LoggerFactory.getLogger(WechatSsoApiController.class);

    @Autowired
    private WechatUtils wechatUtils;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private TxUserService txUserService;

    @Autowired
    private TxUserTokenService txUserTokenService;

    @Autowired
    private SmsCodeService smsCodeService;

    @Autowired
    private LuosimaoService luosimaoService;

    @Autowired
    private TxUserInviteService txUserInviteService;

    /**
     * 认证手机登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(String mobile, String type, String verifyCode,
                                     String unionId, String openId, String nickName, String headImgUrl, String captcha,
                                     String inviter) {
        if (StringUtils.isBlank(mobile)) {
            return R.error("手机号不能为空");
        }
        if (StringUtils.isBlank(verifyCode)) {
            return R.error("手机验证码不能为空");
        }

        if (luosimaoService.needCaptcha()) {
//            if (!luosimaoService.isResponseVerified(captcha)) {
            if (StringUtils.isEmpty(captcha)) {
                return R.error("人机识别验证失败");
            }
        }

        if (!smsCodeService.isVerifyCodeMatch(mobile, type, verifyCode)) {
            return R.error("手机号验证码错误或已失败, 请重新获取");
        }

        TxUserEntity userEntity = txUserService.queryByMobile(mobile);
        if (userEntity == null) {
            userEntity = new TxUserEntity();
            userEntity.setMobile(mobile);
            userEntity.setWechatId(openId);
            userEntity.setUnionId(unionId);
            userEntity.setNickName(nickName);
            userEntity.setHeadImgUrl(headImgUrl);
            userEntity.setCreateTime(new Date());
            txUserService.save(userEntity);

            logger.info("created user: {}", userEntity.getUserId());

            if (StringUtils.isNotBlank(inviter)) {
                TxUserEntity inviterUser = txUserService.queryByMobile(inviter);
                if (inviterUser != null) {
                    TxUserInviteEntity userInviteEntity = new TxUserInviteEntity();
                    userInviteEntity.setUserId(userEntity.getUserId());
                    userInviteEntity.setInviterUserId(inviterUser.getUserId());
                    userInviteEntity.setCreateTime(new Date());
                    txUserInviteService.save(userInviteEntity);
                }
            }
        } else {
            if (StringUtils.isNotBlank(unionId)) {
//                if (StringUtils.isNotBlank(userEntity.getUnionId()) &&
//                        StringUtils.equalsIgnoreCase(unionId, userEntity.getUnionId())) {
//                    return R.error("此帐户已绑定其他微信账号!");
//                }

                userEntity.setUnionId(unionId);
            }

            if (StringUtils.isNotBlank(openId)) {
//                if (StringUtils.isNotBlank(userEntity.getWechatId())
//                        && !StringUtils.equalsIgnoreCase(openId, userEntity.getWechatId())) {
//                    return R.error("此帐户已绑定其他微信账号!");
//                }

                userEntity.setWechatId(openId);
            }

            if (StringUtils.isNotBlank(nickName)) {
                userEntity.setNickName(nickName);
            }
            if (StringUtils.isNotBlank(headImgUrl)) {
                userEntity.setHeadImgUrl(headImgUrl);
            }
            txUserService.update(userEntity);
        }

        String token = txUserTokenService.createToken(userEntity.getUserId());

        HttpContextUtils.addCookie(HttpContextUtils.getHttpServletResponse(),
                "txTokenId", token, 60 * 60 * 24 * 7);

        return R.ok();
    }
}
