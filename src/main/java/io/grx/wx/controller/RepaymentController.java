package io.grx.wx.controller;

import static io.grx.common.utils.Constant.PAGE_EXT;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import io.grx.modules.tx.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.grx.common.exception.RRException;
import io.grx.common.utils.DateUtils;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxExtensionEntity;
import io.grx.modules.tx.entity.TxRepaymentEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.enums.ExtensionStatus;
import io.grx.modules.tx.enums.RepaymentStatus;
import io.grx.modules.tx.enums.RepaymentType;
import io.grx.modules.tx.enums.TxStatus;

@Controller
@RequestMapping("/rcpt/repayment")
public class RepaymentController extends BaseController {
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-M-d");
    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private TxRepaymentService repaymentService;

    @Autowired
    private TxUserPasswordService passwordService;

    @Autowired
    private TxExtensionService txExtensionService;

    @Autowired
    private TxMessageService txMessageService;

    @Autowired
    private TxUserBalanceService txUserBalanceService;

    @Autowired
    private TxOverdueRecordService txOverdueRecordService;

    @Autowired
    private TxUserService txUserService;

    /**
     * 还款页面
     *
     * @return
     */
    @RequestMapping(value = "/create/{txId}", method = RequestMethod.GET)
    public String createRepaymentPage(Model model, @PathVariable(value = "txId") Long txId) {
        TxUserEntity user = ShiroUtils.getTxUser();

        TxBaseEntity entity = txBaseService.queryObject(txId);

        model.addAttribute("tx", entity);

        TxRepaymentEntity repaymentEntity = repaymentService.getLastRepaymentByTx(txId);

        if (entity.getStatus() == TxStatus.COMPLETED
                || (repaymentEntity != null && repaymentEntity.getStatus() == RepaymentStatus.NEW)) {
            return "redirect:/rcpt/repayment_to_pay_plan/" + txId + PAGE_EXT;
        }

        return TX_TEMPLATE_PATH + "repayment_create";
    }

    /**
     * 创建展期页面
     *
     * @return
     */
    @RequestMapping(value = "/create_extension/{txId}", method = RequestMethod.GET)
    public String createExtensionPage(Model model, @PathVariable(value = "txId") Long txId) {
        TxUserEntity user = ShiroUtils.getTxUser();

        TxBaseEntity entity = txBaseService.queryObject(txId);

        model.addAttribute("tx", entity);

        TxExtensionEntity extensionEntity = txExtensionService.getLastExtensionByTx(txId);

        if (entity.getStatus() == TxStatus.COMPLETED
                || (extensionEntity != null && extensionEntity.getStatus() == ExtensionStatus.NEW)) {
            return "redirect:/rcpt/repayment/extension/" + extensionEntity.getTxId() + PAGE_EXT;
        }

        List<TxExtensionEntity> extensionEntityList = txExtensionService.getExtensionsByTx(txId);
        model.addAttribute("extensionList", extensionEntityList);

        return TX_TEMPLATE_PATH + "repayment_create_extension";
    }


    /**
     * 更新展期API
     *
     * @return
     */
    @RequestMapping(value = "/extension/{extensionId}", method = RequestMethod.PATCH)
    @ResponseBody
    @Transactional
    public R updateExtension(@PathVariable(value = "extensionId") Long extensionId, String password,
                             int status) {
        TxUserEntity user = ShiroUtils.getTxUser();

        if (!passwordService.isPasswordMatched(user.getUserId(), password)) {
            return R.error("交易密码错误");
        }

        TxExtensionEntity extensionEntity = txExtensionService.queryObject(extensionId);
        if (extensionEntity == null) {
            throw new RRException("Invalid request");
        }

        return updateExtension(extensionEntity, ExtensionStatus.valueOf(status));
    }

    private R updateExtension(TxExtensionEntity extensionEntity, ExtensionStatus status) {
        if (status == ExtensionStatus.CONFIRMED) {
            if (extensionEntity.getFeeAmount() != null
                    && extensionEntity.getFeeAmount().doubleValue() > 0 && !txUserBalanceService.useBalanceByPayExtension
                    (extensionEntity)) {
                return R.ok().put("wxPay", true);
            }
        }
        txExtensionService.updateStatus(extensionEntity, status, ShiroUtils.getTxUser());

        try {
            if (extensionEntity.getStatus() == ExtensionStatus.CONFIRMED) {
                txMessageService.sendMsgForConfirmingExtension(extensionEntity);
            } else if (extensionEntity.getStatus() == ExtensionStatus.REJECTED) {
                txMessageService.sendMsgForRejectingExtension(extensionEntity);
            }
        } catch (Throwable t) {
            logger.error("Failed to send wechat message", t);
        }

        return R.ok();
    }

    /**
     * 还款页面
     *
     * @return
     */
    @RequestMapping(value = "/to_pay_view/{txId}", method = RequestMethod.GET)
    public String createRepaymentViewPage(Model model, @PathVariable(value = "txId") Long txId) {
        TxUserEntity user = ShiroUtils.getTxUser();

        TxBaseEntity entity = txBaseService.queryObject(txId);

        model.addAttribute("tx", entity);

        TxRepaymentEntity repaymentEntity = repaymentService.getLastRepaymentByTx(txId);

        model.addAttribute("repayment", repaymentEntity);
//
//        if (entity.getStatus() == TxStatus.COMPLETED
//                || (repaymentEntity != null && repaymentEntity.getStatus() == RepaymentStatus.NEW)) {
//            return "redirect:/wx/repayment_to_pay";
//        }

        return TX_TEMPLATE_PATH + "repayment_to_pay_view";
    }


    /**
     * 发起还款
     *
     * @return
     */
    @RequestMapping(value = "{txId}", method = RequestMethod.POST)
    @ResponseBody
    public R confirmTransaction(@PathVariable("txId") Long txId, int repaymentType, String password) {
        TxUserEntity user = ShiroUtils.getTxUser();

        TxBaseEntity entity = txBaseService.queryObject(txId);

        if (!passwordService.isPasswordMatched(user.getUserId(), password)) {
            return R.error("交易密码错误");
        }

        if (entity.getStatus() == TxStatus.COMPLETED
                || !user.getUserId().equals(entity.getBorrowerUserId())) {
            return R.error("非法请求");
        }

        TxRepaymentEntity repaymentEntity = new TxRepaymentEntity();
        repaymentEntity.setTxId(txId);
        repaymentEntity.setRepaymentType(RepaymentType.valueOf(repaymentType));
        repaymentEntity.setStatus(RepaymentStatus.NEW);
        repaymentEntity.setCreateTime(new Date());

        repaymentService.save(repaymentEntity);

        txMessageService.sendMsgForRepay(repaymentEntity);

        return R.ok();
    }

    /**
     * 发起展期
     *
     * @return
     */
    @RequestMapping(value = "/extension/{txId}", method = RequestMethod.POST)
    @ResponseBody
    public R createExtension(@PathVariable("txId") Long txId, Integer extendAmount, int rate,
                             String newEndDate, String password) {
        TxUserEntity user = ShiroUtils.getTxUser();

        TxBaseEntity entity = txBaseService.queryObject(txId);

        if (!passwordService.isPasswordMatched(user.getUserId(), password)) {
            return R.error("交易密码错误");
        }

        if (entity.getStatus() == TxStatus.COMPLETED
                || !user.getUserId().equals(entity.getLenderUserId())) {
            return R.error("非法请求");
        }

        if (extendAmount != null) {
            if (extendAmount > entity.getOutstandingAmount()) {
                return R.error("展期金额不能大于待还金额");
            }
            if (extendAmount <= 0) {
                return R.error("展期金额必须大于0");
            }
        }

        LocalDate newEndLocalDate = LocalDate.parse(newEndDate, format);
        Date newEnd = Date.from(newEndLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        long days = DateUtils.daysBetween(entity.getEndDate(), newEnd);
        if (days <= 0) {
            return R.error("展期日期必须大于原结束日期");
        }

        if (days > 10) {
            return R.error("展期期限不能超过10天");
        }

        TxExtensionEntity extensionEntity = new TxExtensionEntity();
        extensionEntity.setTxId(entity.getTxId());
        if (extendAmount != null) {
            extensionEntity.setExtendAmount(extendAmount);
        } else {
            extensionEntity.setExtendAmount(entity.getAmount());
        }

        extensionEntity.setOldRate(entity.getRate());
        extensionEntity.setRate(rate);
        extensionEntity.setCreateTime(new Date());
        extensionEntity.setOldEndDate(entity.getEndDate());
        extensionEntity.setNewEndDate(newEnd);
        extensionEntity.setStatus(ExtensionStatus.NEW);
        if (entity.getFeeAmount().doubleValue() > 0) {
            if (!txExtensionService.isFreeExtensionLenderMobile(user.getMobile())) {
                extensionEntity.setFeeAmount(txExtensionService.getExtensionFee());
            } else {
                extensionEntity.setFeeAmount(BigDecimal.ZERO);
            }
        } else {
            extensionEntity.setFeeAmount(BigDecimal.ZERO);
        }

        txExtensionService.save(extensionEntity);

        txMessageService.sendMsgForPendingExtension(extensionEntity);

        return R.ok();
    }

    /**
     * 还款页面
     *
     * @return
     */
    @RequestMapping(value = "/repay/{txId}", method = RequestMethod.GET)
    public String viewRepayment(Model model, @PathVariable(value = "txId") Long txId) {
        TxUserEntity user = ShiroUtils.getTxUser();

        TxBaseEntity entity = txBaseService.queryObject(txId);

        model.addAttribute("tx", entity);

        TxExtensionEntity extensionEntity = txExtensionService.getLastExtensionByTx(txId);
        TxRepaymentEntity repaymentEntity = repaymentService.getLastRepaymentByTx(txId);

        model.addAttribute("txExtension", extensionEntity);
        model.addAttribute("txRepayment", repaymentEntity);

        return TX_TEMPLATE_PATH + "view_repayment";
    }

    /**
     * 展期列表页面
     *
     * @return
     */
    @RequestMapping(value = "/extension/{txId}.html", method = RequestMethod.GET)
    public String repayExtensionDetail(Model model, @PathVariable(value = "txId") Long txId) {
        TxUserEntity user = ShiroUtils.getTxUser();

        TxBaseEntity entity = txBaseService.queryObject(txId);

        model.addAttribute("tx", entity);

        List<TxExtensionEntity> extensionEntityList = txExtensionService.getExtensionsByTx(txId);
        model.addAttribute("extensionList", extensionEntityList);

        if (CollectionUtils.isNotEmpty(extensionEntityList)) {
            model.addAttribute("txExtension", extensionEntityList.get(0));
        }

        return TX_TEMPLATE_PATH + "repayment_extension";
    }

    /**
     * 发起还款
     *
     * @return
     */
    @RequestMapping(value = "{repaymentId}", method = RequestMethod.PATCH)
    @ResponseBody
    @Transactional
    public R updateRepayment(@PathVariable("repaymentId") Long repaymentId, int status, String pass) {
        TxUserEntity user = ShiroUtils.getTxUser();

        if (!passwordService.isPasswordMatched(user.getUserId(), pass)) {
            return R.error("交易密码错误");
        }

        TxRepaymentEntity repaymentEntity = repaymentService.queryObject(repaymentId);

        if (repaymentEntity == null) {
            return R.error("非法请求");
        }

        TxBaseEntity entity = txBaseService.queryObject(repaymentEntity.getTxId());

        if (repaymentEntity.getStatus() != RepaymentStatus.NEW
                || !user.getUserId().equals(entity.getLenderUserId())) {
            return R.error("非法请求");
        }

        repaymentService.updateStatus(repaymentEntity, RepaymentStatus.valueOf(status));

        if (repaymentEntity.getStatus() == RepaymentStatus.CONFIRMED) {
            // cancel latest extension if any
            TxExtensionEntity extensionEntity = txExtensionService.getLastExtensionByTx(repaymentEntity.getTxId());
            if (extensionEntity != null && extensionEntity.getStatus() == ExtensionStatus.NEW) {
                txExtensionService.updateStatus(extensionEntity, ExtensionStatus.CANCELLED, ShiroUtils.getTxUser());
            }

//            TxOverdueRecordEntity txOverdueRecordEntity = txOverdueRecordService.queryLatestByTxId(repaymentEntity.getTxId());
//            if (txOverdueRecordEntity != null) {
//                txOverdueRecordEntity.setOverdueEndDate(new Date());
//                txOverdueRecordService.update(txOverdueRecordEntity);
//            }

//            txOverdueRecordService.completeOverdueRecord(repaymentEntity.getTxId());
        }

        try {
            if (repaymentEntity.getStatus() == RepaymentStatus.REJECTED) {
                txMessageService.sendMsgForRejectingRepay(repaymentEntity);
            } else if (repaymentEntity.getStatus() == RepaymentStatus.CONFIRMED) {
                txMessageService.sendMsgForConfirmingRepay(repaymentEntity);
            }
        } catch (Throwable t) {
            logger.error("Failed to send wechat message", t);
        }

        return R.ok();
    }

    /**
     * 出借销账
     *
     * @return
     */
    @RequestMapping(value = "/complete/{txId}", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public R lenderCompleteTransaction(@PathVariable("txId") Long txId, String password) {
        TxUserEntity user = ShiroUtils.getTxUser();

        TxBaseEntity entity = txBaseService.queryObject(txId);

        if (!passwordService.isPasswordMatched(user.getUserId(), password)) {
            return R.error("交易密码错误");
        }

        if (entity.getStatus() == TxStatus.COMPLETED
                || !user.getUserId().equals(entity.getLenderUserId())) {
            return R.error("非法请求");
        }

        entity.setRepayDate(new Date());
        entity.setStatus(TxStatus.COMPLETED);

        txBaseService.update(entity);

        txOverdueRecordService.completeOverdueRecord(txId);

        try {
            txMessageService.sendMsgForLenderCompleteTx(entity);
        } catch (Throwable t) {
            logger.error("Error in sending wechat message", t);
        }

        return R.ok();
    }
}
