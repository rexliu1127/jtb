package io.grx.wx.controller;

import io.grx.common.utils.Query;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.tx.entity.*;
import io.grx.modules.tx.service.TxUserBalanceService;
import io.grx.modules.tx.service.TxUserPasswordService;
import io.grx.modules.tx.service.TxUserService;
import io.grx.modules.tx.service.TxUserWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/rcpt")
public class UserBalanceApiController {

    @Autowired
    private TxUserService txUserService;

    @Autowired
    private TxUserBalanceService txUserBalanceService;

    @Autowired
    private TxUserWithdrawService txUserWithdrawService;

    @Autowired
    private TxUserPasswordService passwordService;

    /**
     * 提现API
     */
    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> withdraw(Double amount, String password) {
        TxUserEntity user = ShiroUtils.getTxUser();
        TxUserBalanceEntity userBalanceEntity = txUserBalanceService.queryObject(user.getUserId());

        if (userBalanceEntity.getBalance().doubleValue() < amount) {
            return R.error("余额不足");
        }

        TxUserPasswordEntity passwordEntity = passwordService.queryObject(user.getUserId());
        if (passwordEntity == null) {
            return R.error("用户还没有设置交易密码");
        }

        if (!passwordService.isPasswordMatched(user.getUserId(), password)) {
            return R.error("交易密码错误");
        }

        BigDecimal feeRate = txUserBalanceService.getWithdrawalFeeRate();
        BigDecimal minAmount = txUserBalanceService.getWithdrawalMinAmount();
        BigDecimal minFee = txUserBalanceService.getWithdrawalMinFee();
        BigDecimal maxFee = txUserBalanceService.getWithdrawalMaxFee();

        if (amount < minAmount.doubleValue()) {
            return R.error("最小提现金额为" + minAmount + "元");
        }

        BigDecimal amountDecimal = new BigDecimal(String.valueOf(amount));
        BigDecimal feeAmount = amountDecimal.multiply(feeRate)
                .setScale(2, RoundingMode.HALF_UP);
        if (minFee.doubleValue() > 0 && feeAmount.doubleValue() < minFee.doubleValue()) {
            feeAmount = minFee;
        }
        if (maxFee.doubleValue() > 0 && feeAmount.doubleValue() > maxFee.doubleValue()) {
            feeAmount = maxFee;
        }

        txUserWithdrawService.withdraw(user.getUserId(), amountDecimal, feeAmount);
        return R.ok();
    }


    /**
     * 我的提现
     */
    @RequestMapping(value = "/withdrawal_list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> withdraw(@RequestParam Map<String, Object> params) {
        TxUserEntity user = ShiroUtils.getTxUser();

        Query query = new Query(params);
        query.put("userId", user.getUserId());

        return R.ok().put("page", txUserWithdrawService.queryList(params));
    }
}
