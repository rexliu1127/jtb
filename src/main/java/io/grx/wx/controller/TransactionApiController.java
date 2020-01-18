package io.grx.wx.controller;


import static java.time.temporal.ChronoUnit.DAYS;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import io.grx.common.exception.RRException;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.entity.TxUserPasswordEntity;
import io.grx.modules.tx.enums.TxStatus;
import io.grx.modules.tx.enums.UsageType;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxMessageService;
import io.grx.modules.tx.service.TxUserBalanceService;
import io.grx.modules.tx.service.TxUserPasswordService;
import io.grx.modules.tx.service.TxUserRelationService;
import io.grx.modules.tx.service.TxUserService;

@RestController
@RequestMapping("/rcpt")
public class TransactionApiController extends BaseController {

    private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private TxUserService txUserService;

    @Autowired
    private TxUserRelationService txUserRelationService;

    @Autowired
    private TxUserPasswordService passwordService;

    @Autowired
    private TxMessageService txMessageService;

    @Autowired
    private TxUserBalanceService txUserBalanceService;


    /**
     * 打借条
     */
    @RequestMapping(value = "/apply_transaction", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> applyTransaction(Integer type, String amount, String beginDate, String endDate,
                                                String rate,
                                                String otherName, String remark, Integer usageType,
                                                String usageRemark, String password,
                                                HttpServletRequest request) {
        if (StringUtils.isBlank(amount)) {
            return R.error("借款金额不能为空");
        }
        if (StringUtils.isBlank(otherName)) {
            return R.error("对方姓名不能为空");
        }
        int txAmount = NumberUtils.toInt(amount, -1);
        if (txAmount <= 0) {
            return R.error("借条金额必须大于0");
        }

        TxUserEntity user = getUser(request);

        TxUserPasswordEntity passwordEntity = passwordService.queryObject(user.getUserId());
        if (passwordEntity == null) {
            return R.error("用户还没有设置交易密码");
        }

        if (!passwordService.isPasswordMatched(user.getUserId(), password)) {
            return R.error("交易密码错误");
        }

        LocalDate beginLocalDate = LocalDate.parse(beginDate, format);
        LocalDate endLocalDate = LocalDate.parse(endDate, format);
        if (DAYS.between(beginLocalDate, endLocalDate) < 0) {
            return R.error("结束日期不能早于开始日期");
        }

        TxBaseEntity entity = new TxBaseEntity();
        BigDecimal txFee = txBaseService.getTransactionFee();

        String txUuid = txBaseService.getNewTxUuid();
        entity.setTxUuid(txUuid);
        entity.setAmount(txAmount);
        entity.setRate(NumberUtils.toInt(rate, 0));

        entity.setBeginDate(Date.from(beginLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        entity.setEndDate(Date.from(endLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        entity.setRemark(remark);
        entity.setUsageType(UsageType.valueOf(usageType));
        entity.setUsageRemark(usageRemark);
        entity.setCreateUserId(user.getUserId());
        entity.setCreateTime(new Date());

        if (type == 1) {

            if (txBaseService.isFreeTx(user.getIdNo(), otherName)) {
                txFee = BigDecimal.ZERO;
            }
            entity.setBorrowerUserId(user.getUserId());
            entity.setLenderName(otherName);
            entity.setBorrowerName(user.getName());
            entity.setBorrowerSignImgPath(user.getSignImgPath());
            if (txFee.doubleValue() > 0) {
                entity.setStatus(TxStatus.UNPAID);
            } else {
                entity.setStatus(TxStatus.NEW);
            }
        } else {
            entity.setLenderUserId(user.getUserId());
            entity.setBorrowerName(otherName);
            entity.setLenderName(user.getName());
            entity.setLenderSignImgPath(user.getSignImgPath());
            entity.setStatus(TxStatus.NEW);

            if (txBaseService.isFreeTxLenderMobile(user.getMobile())) {
                txFee = BigDecimal.ZERO;
            }
        }

        entity.setInterest(txBaseService.calculateInterest(entity));
        entity.setFeeAmount(txFee);
        txBaseService.save(entity);

        return R.ok().put("tx", entity);
    }

    /**
     * 确定借条
     *
     * @return
     */
    @RequestMapping(value = "/transaction/confirm/{txUuid}", method = RequestMethod.POST)
    public R confirmTransaction(@PathVariable("txUuid") String txUuid, String password) {
        TxUserEntity user = ShiroUtils.getTxUser();

        TxBaseEntity entity = txBaseService.queryByUuid(txUuid);

        if (!passwordService.isPasswordMatched(user.getUserId(), password)) {
            return R.error("交易密码错误");
        }

        if (entity == null || (entity.getStatus() != TxStatus.NEW && entity.getStatus() != TxStatus.REJECTED)) {
            return R.error("借条无效");
        }
        if (user.getUserId().equals(entity.getCreateUserId())) {
            return R.error("不能确认自己创建的借条");
        }

        if (StringUtils.isBlank(user.getName())) {
            return R.error("请先绑定银行卡").put("errorId", "1");
        }

        if (entity.getBorrowerUserId() == null
                && !StringUtils.equalsIgnoreCase(user.getName(), entity.getBorrowerName())) {
            return R.error("借款人姓名跟你本人姓名不一致");
        }

        if (entity.getLenderUserId() == null
                && !StringUtils.equalsIgnoreCase(user.getName(), entity.getLenderName())) {
            return R.error("出借人姓名跟你本人姓名不一致");
        }

        if ((entity.getBorrowerUserId() == null || entity.getBorrowerUserId().equals(user.getUserId())) &&
                entity.getFeeAmount() != null &&
                entity.getFeeAmount().doubleValue() > 0 &&
                !txUserBalanceService.useBalanceByPayTx(entity)) {
            // 余额支付失败
            return R.ok().put("wxPay", true);
        }

        try {
            txBaseService.confirmTx(entity, user);
        } catch (Exception e) {
            logger.error("[IMPORTANT] Failed to update TX status after paid by balance: {}", new Gson().toJson(entity), e);
            return R.error("系统错误");
        }


        return R.ok();
    }


    /**
     * 确定借条
     *
     * @return
     */
    @RequestMapping(value = "/transaction/check_confirm/{txUuid}", method = RequestMethod.POST)
    public R checkConfirmTransaction(@PathVariable("txUuid") String txUuid, String password) {
        TxUserEntity user = ShiroUtils.getTxUser();

        TxBaseEntity entity = txBaseService.queryByUuid(txUuid);

        if (!passwordService.isPasswordMatched(user.getUserId(), password)) {
            return R.error("交易密码错误");
        }

        if (entity == null || entity.getStatus() != TxStatus.NEW) {
            return R.error("借条无效");
        }
        if (user.getUserId().equals(entity.getCreateUserId())) {
            return R.error("不能确认自己创建的借条");
        }

        if (StringUtils.isBlank(user.getName())) {
            return R.error("请先绑定银行卡").put("errorId", "1");
        }

        if (entity.getBorrowerUserId() == null
                && !StringUtils.equalsIgnoreCase(user.getName(), entity.getBorrowerName())) {
            return R.error("借款人姓名跟你本人姓名不一致");
        }

        if (entity.getLenderUserId() == null
                && !StringUtils.equalsIgnoreCase(user.getName(), entity.getLenderName())) {
            return R.error("出借人姓名跟你本人姓名不一致");
        }

        return R.ok();
    }


    /**
     * 支付借条服务费
     *
     * @return
     */
    @RequestMapping(value = "/transaction/pay/{txId}", method = RequestMethod.POST)
    public R payTransaction(@PathVariable("txId") Long txId) {
        TxUserEntity user = ShiroUtils.getTxUser();

        TxBaseEntity entity = txBaseService.queryObject(txId);

        if (entity == null) {
            return R.error("借条无效");
        }

        if (!user.getUserId().equals(entity.getBorrowerUserId())) {
            return R.error("出借人不需要支付服务费");
        }

        boolean createdByBorrower = entity.getCreateUserId().equals(entity.getBorrowerUserId());
        boolean createdByLender = entity.getCreateUserId().equals(entity.getLenderUserId());

        if ((createdByBorrower && entity.getStatus() != TxStatus.UNPAID)
                || (createdByLender && entity.getStatus() != TxStatus.NEW)) {
            return R.error("借条已支付过");
        }

        if (StringUtils.isBlank(user.getName())) {
            return R.error("请先绑定银行卡").put("errorId", "1");
        }

        if (!txUserBalanceService.useBalanceByPayTx(entity)) {
            // 余额支付失败
            return R.ok().put("wxPay", true);
        }

        try {
            if (entity.getStatus() == TxStatus.UNPAID) {
                entity.setStatus(TxStatus.NEW);
                txBaseService.update(entity);
            } else if (entity.getStatus() == TxStatus.NEW) {
                txBaseService.confirmTx(entity, user);
            }
        } catch (Exception e) {
            logger.error("[IMPORTANT] Failed to update TX status after paid by balance: {}", new Gson().toJson(entity));
            return R.error("系统错误");
        }

        return R.ok().put("tx", entity);
    }

    /**
     * 确定借条
     *
     * @return
     */
    @RequestMapping(value = "/transaction/cancel/{txUuid}", method = RequestMethod.POST)
    public R cancelTransaction(@PathVariable("txUuid") String txUuid, String password) {
        TxUserEntity user = ShiroUtils.getTxUser();

        if (!passwordService.isPasswordMatched(user.getUserId(), password)) {
            return R.error("交易密码错误");
        }

        TxBaseEntity entity = txBaseService.queryByUuid(txUuid);

        if (entity == null || entity.getStatus() != TxStatus.NEW) {
            return R.error("借条无效");
        }
        if (StringUtils.isBlank(user.getName())) {
            return R.error("请先绑定银行卡").put("errorId", "1");
        }

        if (!user.getUserId().equals(entity.getCreateUserId())) {
            if (entity.getBorrowerUserId() == null
                    && !StringUtils.equalsIgnoreCase(user.getName(), entity.getBorrowerName())) {
                return R.error("借款人姓名跟你本人姓名不一致");
            }

            if (entity.getLenderUserId() == null
                    && !StringUtils.equalsIgnoreCase(user.getName(), entity.getLenderName())) {
                return R.error("出借人姓名跟你本人姓名不一致");
            }
        }

//        logger.info("DELETE TX: {} by user {}", new Gson().toJson(entity), user.getUserId());

//        txBaseService.delete(entity.getTxId());

        entity.setStatus(TxStatus.REJECTED);
        txBaseService.update(entity);

//        try {
//            if (entity.getBorrowerUserId() != null && entity.getStatus() == TxStatus.NEW
//                    && entity.getFeeAmount().doubleValue() > 0) {
//                txUserBalanceService.addBalanceByDeletingTx(entity);
//            }
//        } catch (Exception e) {
//            logger.error("[IMPORTANT] failed add user balance");
//        }

        txMessageService.sendMsgForCancelingTransaction(entity);

        return R.ok();
    }



    /**
     * 删除借条
     *
     * @return
     */
    @RequestMapping(value = "/transaction/{txId}", method = RequestMethod.DELETE)
    public R deleteTransaction(HttpServletRequest request, @PathVariable("txId") Long txId) {
        TxUserEntity user = getUser(request);

        TxBaseEntity entity = txBaseService.queryObject(txId);

        if (entity == null) {
            return R.error("借条无效");

        }
        if (!user.getUserId().equals(entity.getCreateUserId())) {
            throw new RRException("未授权操作", 401);
        }

        if (entity.getStatus() != TxStatus.UNPAID && entity.getStatus() != TxStatus.NEW
                && entity.getStatus() != TxStatus.REJECTED) {
            return R.error("不能删除已确认的借条");
        }

        txBaseService.delete(txId);

        try {
            if (user.getUserId().equals(entity.getBorrowerUserId())
                    && (entity.getStatus() == TxStatus.NEW || entity.getStatus() == TxStatus.REJECTED)
                    && entity.getFeeAmount().doubleValue() > 0) {
                txUserBalanceService.addBalanceByDeletingTx(entity);
            }
        } catch (Exception e) {
            logger.error("[IMPORTANT] failed add user balance");
        }

        return R.ok();
    }


    /**
     * 删除借条
     *
     * @return
     */
    @RequestMapping(value = "/transaction/check_free_tx", method = RequestMethod.GET)
    public R deleteTransaction(@PathVariable("lenderName") Long lenderName) {
        TxUserEntity user = ShiroUtils.getTxUser();

        return R.ok();
    }
}
