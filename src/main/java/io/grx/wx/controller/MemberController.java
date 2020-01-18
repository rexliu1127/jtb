package io.grx.wx.controller;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.QRCodeUtils;
import io.grx.modules.oss.cloud.CloudStorageService;
import io.grx.modules.oss.cloud.OSSFactory;
import io.grx.modules.tx.entity.TxUserBalanceEntity;
import io.grx.modules.tx.service.*;
import io.grx.wx.annotation.WxJsSign;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.grx.common.exception.RRException;
import io.grx.common.service.impl.LuosimaoService;
import io.grx.common.utils.CacheUtils;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.wx.dto.TxOverdueSummaryDto;
import io.grx.wx.dto.TxSummaryDto;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/rcpt")
public class MemberController extends BaseController {
    @Autowired
    private TxUserService txUserService;

    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private TxOverdueRecordService txOverdueRecordService;

    @Autowired
    private TxUserBalanceService txUserBalanceService;

    @Autowired
    private TxUserInviteService txUserInviteService;

    @Autowired
    private TxUserWithdrawService txUserWithdrawService;

    @Autowired
    private TxUserRewardService txUserRewardService;

    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private LuosimaoService luosimaoService;

    private CloudStorageService cloudStorageService;

    /**
     * 用户详情页
     *
     * @return
     */
    @RequestMapping(value = "/member.html", method = RequestMethod.GET)
    public String memberPage(Model model, HttpServletRequest request) {
        TxUserEntity user = getUser(request);

        model.addAttribute("user", user);

        if (StringUtils.isNotBlank(user.getName())) {

            TxUserBalanceEntity userBalanceEntity = txUserBalanceService.queryObject(user.getUserId());
            model.addAttribute("balance", userBalanceEntity != null ? userBalanceEntity.getBalance().doubleValue() :
                    0.0);
        } else {
            model.addAttribute("balance", 0.0);
        }

        model.addAttribute("teamMemberCount", txUserInviteService.getTeamUserCount(user.getUserId()));
        model.addAttribute("userReward", txUserRewardService.sumUserReward(user.getUserId()));
        model.addAttribute("userWithdrawal", txUserWithdrawService.sumUserWithdrawal(user.getUserId()));

        return TX_TEMPLATE_PATH + "member";
    }


    /**
     * 好友
     *
     * @return
     */
    @RequestMapping(value = "/member/friend.html", method = RequestMethod.GET)
    public String friendPage(Model model) {
        TxUserEntity user = ShiroUtils.getTxUser();

        model.addAttribute("total", txUserService.getFriendTotal(user.getUserId()));

        return TX_TEMPLATE_PATH + "member/friend";
    }

    /**
     * 用户详情页
     *
     * @return
     */
    @RequestMapping(value = "/member/setup.html", method = RequestMethod.GET)
    public String setUpPage(Model model) {
        TxUserEntity user = ShiroUtils.getTxUser();

        return TX_TEMPLATE_PATH + "member/setup";
    }


    /**
     * 我的信用页
     *
     * @return
     */
    @RequestMapping(value = "/member/credit/{userId}.html", method = RequestMethod.GET)
    public String creditPage(Model model, @PathVariable(value = "userId") Long userId) {
        TxUserEntity user = ShiroUtils.getTxUser();
        if (!user.getUserId().equals(userId) && !txUserService.isFriend(user.getUserId(), userId)) {
            throw new RRException("Invalid request", 401);
        }

        TxSummaryDto borrowerSummary = txBaseService.queryBorrowerSummary(userId);
        TxSummaryDto lenderSummary = txBaseService.queryLenderSummary(userId);
        TxOverdueSummaryDto overdueSummary = txBaseService.queryOverdueSummary(userId);

        model.addAttribute("borrowerSummary", borrowerSummary);
        model.addAttribute("lenderSummary", lenderSummary);
        model.addAttribute("overdueSummary", overdueSummary);
        model.addAttribute("userId", userId);
        model.addAttribute("user", txUserService.queryObject(userId));

        return TX_TEMPLATE_PATH + "member/credit";
    }


    /**
     * 我的信用页
     *
     * @return
     */
    @RequestMapping(value = "/member/credit_detail/{userId}.html", method = RequestMethod.GET)
    public String creditDetailPage(Model model, @PathVariable(value = "userId") Long userId,
                                   Long txId) {
        TxUserEntity user = ShiroUtils.getTxUser();

        checkViewCreditAcl(user, userId, txId);

        TxSummaryDto borrowerSummary = txBaseService.queryBorrowerSummary(userId);
        TxSummaryDto lenderSummary = txBaseService.queryLenderSummary(userId);
        TxOverdueSummaryDto overdueSummary = txBaseService.queryOverdueSummary(userId);

        model.addAttribute("borrowerSummary", borrowerSummary);
        model.addAttribute("lenderSummary", lenderSummary);
        model.addAttribute("overdueSummary", overdueSummary);
        model.addAttribute("userId", userId);

        model.addAttribute("txId", txId);

        model.addAttribute("user", txUserService.queryObject(userId));

        return TX_TEMPLATE_PATH + "member/credit_detail";
    }

    private void checkViewCreditAcl(TxUserEntity user, Long userId, Long txId) {
        if (txId != null) {
            TxBaseEntity txBaseEntity = txBaseService.queryObject(txId);
            if (txBaseEntity == null || !txBaseEntity.getBorrowerUserId().equals(userId)
                    || (StringUtils.isNotBlank(user.getName()) && !StringUtils.equals(txBaseEntity.getLenderName(), user
                    .getName()))) {
                throw new RRException("Invalid request 1", 401);
            }
        } else {

            String creditUserId = cacheUtils.get(creditCacheKey(ShiroUtils.getUserId()));

            if (!user.getUserId().equals(userId) && !txUserService.isFriend(user.getUserId(), userId)
                    && !StringUtils.equals(creditUserId, ObjectUtils.toString(userId))) {
                throw new RRException("Invalid request 2", 401);
            }
        }

    }


    /**
     * 信用-借款记录
     *
     * @return
     */
    @RequestMapping(value = "/member/credit_detail_borrower/{userId}.html", method = RequestMethod.GET)
    public String creditDetailForBorrowerPage(Model model, @PathVariable(value = "userId") Long userId,
                                              Long txId) {
        TxUserEntity user = ShiroUtils.getTxUser();
        checkViewCreditAcl(user, userId, txId);
        model.addAttribute("userId", userId);
        model.addAttribute("txId", txId);
        model.addAttribute("user", txUserService.queryObject(userId));

        return TX_TEMPLATE_PATH + "member/credit_detail_borrower";
    }

    /**
     * 信用-借款记录
     *
     * @return
     */
    @RequestMapping(value = "/member/credit_detail_lender/{userId}.html", method = RequestMethod.GET)
    public String creditDetailForLenderPage(Model model, @PathVariable(value = "userId") Long userId,Long txId) {
        TxUserEntity user = ShiroUtils.getTxUser();
        checkViewCreditAcl(user, userId, txId);
        model.addAttribute("userId", userId);
        model.addAttribute("txId", txId);

        model.addAttribute("user", txUserService.queryObject(userId));

        return TX_TEMPLATE_PATH + "member/credit_detail_lender";
    }


    /**
     * 信用-借款记录
     *
     * @return
     */
    @RequestMapping(value = "/member/credit_detail_overdue/{userId}.html", method = RequestMethod.GET)
    public String creditDetailForOverduePage(Model model, @PathVariable(value = "userId") Long userId, Long txId) {
        TxUserEntity user = ShiroUtils.getTxUser();
        checkViewCreditAcl(user, userId, txId);
        model.addAttribute("userId", userId);

        model.addAttribute("txId", txId);
        model.addAttribute("user", txUserService.queryObject(userId));
        return TX_TEMPLATE_PATH + "member/credit_detail_overdue";
    }

    /**
     * 关于
     *
     * @return
     */
    @RequestMapping(value = "/member/about.html", method = RequestMethod.GET)
    public String aboutPage(Model model) {
        TxUserEntity user = ShiroUtils.getTxUser();

        model.addAttribute("user", user);

        return TX_TEMPLATE_PATH + "member/about";
    }


    /**
     * 平台介绍
     *
     * @return
     */
    @RequestMapping(value = "/member/about_us.html", method = RequestMethod.GET)
    public String aboutUsPage(Model model) {
        TxUserEntity user = ShiroUtils.getTxUser();

        model.addAttribute("user", user);

        return TX_TEMPLATE_PATH + "member/about_us";
    }


    /**
     * 收费方式
     *
     * @return
     */
    @RequestMapping(value = "/member/about_charge.html", method = RequestMethod.GET)
    public String aboutChargePage(Model model) {
        TxUserEntity user = ShiroUtils.getTxUser();

        model.addAttribute("user", user);

        return TX_TEMPLATE_PATH + "member/about_charge";
    }


    /**
     * 收费方式
     *
     * @return
     */
    @RequestMapping(value = "/member/bank_info.html", method = RequestMethod.GET)
    public String bankInfo(Model model) {
        TxUserEntity user = ShiroUtils.getTxUser();

        model.addAttribute("user", user);

        if (StringUtils.isBlank(user.getBankAccount())) {
            return "redirect:/rcpt/bind_bank.html?returnUrl=/rcpt/member/bank_info";
        }

        return TX_TEMPLATE_PATH + "member/bank_info";
    }

    /**
     * 查信用页面
     *
     * @return
     */
    @RequestMapping(value = "/member/check_credit.html", method = RequestMethod.GET)
    public String checkCreditPage(Model model) {
        TxUserEntity user = ShiroUtils.getTxUser();

        if (StringUtils.isBlank(user.getName())) {
            return "redirect:/rcpt/bind_bank.html";
        }

        model.addAttribute("user", user);

        model.addAttribute("needCaptcha", luosimaoService.needCaptcha());
        model.addAttribute("captchaKey", luosimaoService.getSiteKey());

        return TX_TEMPLATE_PATH + "member/check_credit";
    }

    private String creditCacheKey(Long userId) {
        return "check_credit:" + ShiroUtils.getUserId();
    }

    /**
     * 设置签名
     *
     * @return
     */
    @RequestMapping(value = "/member/sign.html", method = RequestMethod.GET)
    public String signPage(Model model, String returnUrl) {
        model.addAttribute("returnUrl", returnUrl);

        return TX_TEMPLATE_PATH + "member/sign";
    }


    private CloudStorageService getCloudStorageService() {
        if (cloudStorageService == null) {
            synchronized (this) {
                if (cloudStorageService == null) {
                    cloudStorageService = OSSFactory.build();
                }
            }
        }
        return cloudStorageService;
    }


    @RequestMapping(value = "/member/sign.png", method = RequestMethod.GET)
    public void loadUserImage(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/png");

        TxUserEntity userEntity = ShiroUtils.getTxUser();

        if (StringUtils.isNotBlank(userEntity.getSignImgPath())) {
            InputStream is = null;
            try {
                is = getCloudStorageService().get("upload/" + userEntity.getSignImgPath());

                IOUtils.copy(is, response.getOutputStream());
            } catch (Exception e) {
                logger.error("Failed to load image by path: " + userEntity.getSignImgPath(), e);
            }
        }
    }


    /**
     * 查看邀请码
     *
     * @return
     */
    @WxJsSign
    @RequestMapping(value = "/member/invite_code.html", method = RequestMethod.GET)
    public String inviteCodePage(Model model, String returnUrl) {
        model.addAttribute("returnUrl", returnUrl);

        return TX_TEMPLATE_PATH + "member/invite_code";
    }

    /**
     * 二维码
     */
    @RequestMapping("/member/share/{mobile}.png")
    public void getConfirmTxQrCode(HttpServletResponse response, @PathVariable("mobile") String mobile) throws
            ServletException, IOException {
        response.setContentType("image/png");

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();

            String text = HttpContextUtils.getRequestBaseUrl() + "/rcpt/index.html?inviter=" + mobile;
            QRCodeUtils.createQRImage(text, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 我的团队
     *
     * @return
     */
    @WxJsSign
    @RequestMapping(value = "/member/team.html", method = RequestMethod.GET)
    public String teamPage(Model model, String returnUrl) {
        return TX_TEMPLATE_PATH + "member/team";
    }


    /**
     * 我的奖励
     *
     * @return
     */
    @WxJsSign
    @RequestMapping(value = "/member/reward.html", method = RequestMethod.GET)
    public String rewardPage(Model model, String returnUrl) {
        return TX_TEMPLATE_PATH + "member/reward";
    }



    /**
     * 我的提现
     *
     * @return
     */
    @RequestMapping(value = "/member/my_withdrawal.html", method = RequestMethod.GET)
    public String myWithdrawalPage(Model model, String returnUrl) {
        return TX_TEMPLATE_PATH + "member/my_withdrawal";
    }
}
