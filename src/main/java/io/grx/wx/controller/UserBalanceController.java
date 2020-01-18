package io.grx.wx.controller;

import io.grx.common.utils.ShiroUtils;
import io.grx.modules.tx.entity.TxUserBalanceEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxOverdueRecordService;
import io.grx.modules.tx.service.TxUserBalanceService;
import io.grx.modules.tx.service.TxUserService;
import io.grx.wx.annotation.WxJsSign;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/rcpt")
public class UserBalanceController extends BaseController {
    @Autowired
    private TxUserService txUserService;

    @Autowired
    private TxUserBalanceService txUserBalanceService;

    /**
     * 提现页面
     *
     * @return
     */
    @WxJsSign
    @RequestMapping(value = "/withdrawal.html", method = RequestMethod.GET)
    public String withdrawalPage(Model model, String returnUrl) {
        TxUserEntity user = ShiroUtils.getTxUser();
        if (StringUtils.isNotBlank(user.getName())) {

            TxUserBalanceEntity userBalanceEntity = txUserBalanceService.queryObject(user.getUserId());
            model.addAttribute("balance", userBalanceEntity != null ? userBalanceEntity.getBalance().doubleValue() :
                    0.0);
        } else {
            model.addAttribute("balance", 0.0);
        }

        model.addAttribute("feeRate", txUserBalanceService.getWithdrawalFeeRate());
        model.addAttribute("minFee", txUserBalanceService.getWithdrawalMinFee());
        model.addAttribute("maxFee", txUserBalanceService.getWithdrawalMaxFee());
        model.addAttribute("minAmount", txUserBalanceService.getWithdrawalMinAmount());

        return TX_TEMPLATE_PATH + "withdrawal";
    }
}
