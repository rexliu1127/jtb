package io.grx.modules.tx.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import io.grx.common.utils.DateUtils;
import io.grx.common.utils.HttpContextUtils;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxExtensionEntity;
import io.grx.modules.tx.entity.TxRepaymentEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxMessageService;
import io.grx.modules.tx.service.TxUserService;
import io.grx.wx.utils.WechatUtils;

@Service
@Configuration
public class TxMessageServiceImpl implements TxMessageService {

    @Autowired
    private TxUserService txUserService;

    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private WechatUtils wechatUtils;

    @Value("${wechat.cancelMessageTemplateId}")
    private String cancelMessageTemplateId;

    @Value("${wechat.confirmOrderTemplateId}")
    private String confirmOrderTemplateId;

    @Value("${wechat.createExtensionTemplateId}")
    private String createExtensionTemplateId;

    @Value("${wechat.confirmExtensionTemplateId}")
    private String confirmExtensionTemplateId;

    @Value("${wechat.rejectExtensionTemplateId}")
    private String rejectExtensionTemplateId;

    @Value("${wechat.repayTemplateId}")
    private String repayTemplateId;

    @Value("${wechat.rejectRepayTemplateId}")
    private String rejectRepayTemplateId;

    @Value("${wechat.confirmRepayTemplateId}")
    private String confirmRepayTemplateId;

    @Override
    public void sendMsgForCancelingTransaction(final TxBaseEntity txBaseEntity) {
        TxUserEntity creator = txUserService.queryObject(txBaseEntity.getCreateUserId());
        if (StringUtils.isBlank(creator.getWechatId())) {
            return;
        }

        String rejectUsername = "";
        if (txBaseEntity.getCreateUserId().equals(txBaseEntity.getLenderUserId())) {
            rejectUsername = txBaseEntity.getBorrowerName();
        } else {
            rejectUsername = txBaseEntity.getLenderName();
        }

        Map<String, String> params = new HashMap<>();
        params.put("first", "您好, " + rejectUsername + "驳回了您的借条.");
        params.put("keyword1", txBaseEntity.getTxUuid());
        params.put("keyword2", String.format("金额: %1$s元, 利率: %2$s%% 开始日期: %3$s, 结束日期: %4$s",
                txBaseEntity.getAmount(), txBaseEntity.getRate(), DateUtils.formateDate(txBaseEntity.getBeginDate()),
                DateUtils.formateDate(txBaseEntity.getEndDate())));
        params.put("remark", "点击查看借条详情.");

        wechatUtils.sendTemplateMessage(creator.getWechatId(), cancelMessageTemplateId,
                HttpContextUtils.getRequestBaseUrl() + "/rcpt/transaction/" + txBaseEntity.getTxId() + ".html",
                params);
    }

    @Override
    public void sendMsgForExpiredTransaction(final TxBaseEntity txBaseEntity) {
        TxUserEntity creator = txUserService.queryObject(txBaseEntity.getCreateUserId());
        if (StringUtils.isBlank(creator.getWechatId())) {
            return;
        }

        String otherName = "";
        if (txBaseEntity.getCreateUserId().equals(txBaseEntity.getBorrowerUserId())) {
            otherName = txBaseEntity.getLenderName();
        } else {
            otherName = txBaseEntity.getBorrowerName();
        }
        Map<String, String> params = new HashMap<>();
        params.put("first", "您的借条超时未被确认, 已被系统删除!");
        params.put("keyword1", txBaseEntity.getTxUuid());
        params.put("keyword2", String.format("金额: %1$s元, 利率: %2$s%% 开始日期: %3$s, 结束日期: %4$s",
                txBaseEntity.getAmount(), txBaseEntity.getRate(), DateUtils.formateDate(txBaseEntity.getBeginDate()),
                DateUtils.formateDate(txBaseEntity.getEndDate())));

        wechatUtils.sendTemplateMessage(creator.getWechatId(), cancelMessageTemplateId,
                "", params);
    }

    @Override
    public void sendMsgForConfirmingTransaction(final TxBaseEntity txBaseEntity) {
        TxUserEntity creator = txUserService.queryObject(txBaseEntity.getCreateUserId());
        if (StringUtils.isBlank(creator.getWechatId())) {
            return;
        }

        String rejectUsername = "";
        if (txBaseEntity.getCreateUserId().equals(txBaseEntity.getLenderUserId())) {
            rejectUsername = txBaseEntity.getBorrowerName();
        } else {
            rejectUsername = txBaseEntity.getLenderName();
        }

        Map<String, String> params = new HashMap<>();
        params.put("first", "您好, " + rejectUsername + "确认了您的借条.");
        params.put("keyword1", txBaseEntity.getTxUuid());
        params.put("keyword2", "借条");
        params.put("keyword3", String.format("金额: %1$s元, 利率: %2$s%% 开始日期: %3$s, 结束日期: %4$s.",
                txBaseEntity.getAmount(), txBaseEntity.getRate(), DateUtils.formateDate(txBaseEntity.getBeginDate()),
                DateUtils.formateDate(txBaseEntity.getEndDate())));
        params.put("remark", "点击查看详情");

        wechatUtils.sendTemplateMessage(creator.getWechatId(), confirmOrderTemplateId,
                HttpContextUtils.getRequestBaseUrl() + "/rcpt/transaction/" + txBaseEntity.getTxId() + ".html", params);
    }

    @Override
    public void sendMsgForRepay(final TxRepaymentEntity txRepaymentEntity) {
        TxBaseEntity txBaseEntity = txBaseService.queryObject(txRepaymentEntity.getTxId());

        TxUserEntity lender = txUserService.queryObject(txBaseEntity.getLenderUserId());

        Map<String, String> params = new HashMap<>();
        params.put("first", "您有待确认的还款");
        params.put("keyword1", String.format("金额: %1$s元, 利息: %2$s元, 还款方式: %3$s",
                txBaseEntity.getAmount(), txBaseEntity.getInterest(),
                txRepaymentEntity.getRepaymentType().getDisplayName()));
        params.put("keyword2", DateUtils.formateDateTime(txRepaymentEntity.getCreateTime()));
        params.put("remark", "查看详情");

        wechatUtils.sendTemplateMessage(lender.getWechatId(), repayTemplateId,
                HttpContextUtils.getRequestBaseUrl() + "/rcpt/repayment/repay/"
                        + txBaseEntity.getTxId() + ".html", params);
    }

    @Override
    public void sendMsgForConfirmingRepay(final TxRepaymentEntity txRepaymentEntity) {
        TxBaseEntity txBaseEntity = txBaseService.queryObject(txRepaymentEntity.getTxId());

        TxUserEntity borrower = txUserService.queryObject(txBaseEntity.getBorrowerUserId());
        TxUserEntity lender = txUserService.queryObject(txBaseEntity.getLenderUserId());

        Map<String, String> params = new HashMap<>();
        params.put("first", "您好, " + lender.getName() + "确认了您的还款!");
        params.put("keyword1", DateUtils.formateDateTime(txRepaymentEntity.getCreateTime()));
        params.put("keyword2", String.format("%1$s元",
                String.valueOf(txBaseEntity.getAmount() + txBaseEntity.getInterest().doubleValue())));
        params.put("remark", "查看详情");

        wechatUtils.sendTemplateMessage(borrower.getWechatId(), confirmRepayTemplateId,
                HttpContextUtils.getRequestBaseUrl() + "/rcpt/transaction/"
                        + txBaseEntity.getTxId() + ".html", params);
    }

    @Override
    public void sendMsgForRejectingRepay(final TxRepaymentEntity txRepaymentEntity) {
        TxBaseEntity txBaseEntity = txBaseService.queryObject(txRepaymentEntity.getTxId());

        TxUserEntity borrower = txUserService.queryObject(txBaseEntity.getBorrowerUserId());
        TxUserEntity lender = txUserService.queryObject(txBaseEntity.getLenderUserId());

        Map<String, String> params = new HashMap<>();
        params.put("first", "您好, " + lender.getName() + "拒绝了您的还款!");
        params.put("keyword1", String.format("%1$s元",
                String.valueOf(txBaseEntity.getAmount() + txBaseEntity.getInterest().doubleValue())));
        params.put("keyword2", "出借人拒绝");
        params.put("remark", "查看详情");

        wechatUtils.sendTemplateMessage(borrower.getWechatId(), rejectRepayTemplateId,
                HttpContextUtils.getRequestBaseUrl() + "/rcpt/transaction/"
                        + txBaseEntity.getTxId() + ".html", params);
    }

    @Override
    public void sendMsgForPendingExtension(final TxExtensionEntity txExtensionEntity) {
        TxBaseEntity txBaseEntity = txBaseService.queryObject(txExtensionEntity.getTxId());
        TxUserEntity borrower = txUserService.queryObject(txBaseEntity.getBorrowerUserId());

        Map<String, String> params = new HashMap<>();
        params.put("first", "您有待确认的展期!");
        params.put("keyword1", txBaseEntity.getTxUuid());
        params.put("keyword2", "展期");
        params.put("keyword3", String.format("金额: %1$s元, 原到期日期: %2$s, 展期日期: %3$s, 原利率: %4$s%%, 新利率: %5$s%%",
                txExtensionEntity.getExtendAmount(), DateUtils.formateDate(txBaseEntity.getEndDate()),
                DateUtils.formateDate(txExtensionEntity.getNewEndDate()),
                txBaseEntity.getRate(),
                txExtensionEntity.getRate()
                ));
        params.put("remark", "查看详情");

        wechatUtils.sendTemplateMessage(borrower.getWechatId(), createExtensionTemplateId,
                HttpContextUtils.getRequestBaseUrl() + "/rcpt/repayment/extension/" + txBaseEntity.getTxId() + "" +
                        ".html",
                params);
    }

    @Override
    public void sendMsgForConfirmingExtension(final TxExtensionEntity txExtensionEntity) {
        TxBaseEntity txBaseEntity = txBaseService.queryObjectNoAcl(txExtensionEntity.getTxId());
        TxUserEntity borrower = txUserService.queryObject(txBaseEntity.getBorrowerUserId());
        TxUserEntity lender = txUserService.queryObject(txBaseEntity.getLenderUserId());

        Map<String, String> params = new HashMap<>();
        params.put("first", "您好, " + borrower.getName() + "确认了您提交的展期!");
        params.put("keyword1", txBaseEntity.getTxUuid());
        params.put("keyword2", "展期");
        params.put("keyword3", String.format("金额: %1$s元, 原到期日期: %2$s, 展期日期: %3$s, 原利率: %4$s%%, 新利率: %5$s%%",
                txExtensionEntity.getExtendAmount(), DateUtils.formateDate(txBaseEntity.getEndDate()),
                DateUtils.formateDate(txExtensionEntity.getNewEndDate()),
                txBaseEntity.getRate(),
                txExtensionEntity.getRate()
        ));
        params.put("remark", "查看详情");

        wechatUtils.sendTemplateMessage(lender.getWechatId(), confirmExtensionTemplateId,
                HttpContextUtils.getRequestBaseUrl() + "/wx/repayment/extension/" + txBaseEntity.getTxId(), params);
    }

    @Override
    public void sendMsgForRejectingExtension(final TxExtensionEntity txExtensionEntity) {
        TxBaseEntity txBaseEntity = txBaseService.queryObject(txExtensionEntity.getTxId());
        TxUserEntity borrower = txUserService.queryObject(txBaseEntity.getBorrowerUserId());
        TxUserEntity lender = txUserService.queryObject(txBaseEntity.getLenderUserId());

        Map<String, String> params = new HashMap<>();
        params.put("first", "您好, " + borrower.getName() + "拒绝了您的展期!");
        params.put("keyword1", txBaseEntity.getTxUuid());
        params.put("keyword2", String.format("金额: %1$s元, 原到期日期: %2$s, 展期日期: %3$s, 原利率: %4$s%%, 新利率: %5$s%%",
                txExtensionEntity.getExtendAmount(), DateUtils.formateDate(txBaseEntity.getEndDate()),
                DateUtils.formateDate(txExtensionEntity.getNewEndDate()),
                txBaseEntity.getRate(),
                txExtensionEntity.getRate()
        ));
        params.put("remark", "查看详情");

        wechatUtils.sendTemplateMessage(lender.getWechatId(), rejectExtensionTemplateId,
                HttpContextUtils.getRequestBaseUrl() + "/wx/repayment/extension/" + txBaseEntity.getTxId() + ".html",
                params);
    }

    /**
     * 出借人销账
     *
     * @param txBaseEntity
     */
    @Override
    public void sendMsgForLenderCompleteTx(final TxBaseEntity txBaseEntity) {
        TxUserEntity borrower = txUserService.queryObject(txBaseEntity.getBorrowerUserId());
        TxUserEntity lender = txUserService.queryObject(txBaseEntity.getLenderUserId());

        Map<String, String> params = new HashMap<>();
        params.put("first", "您好, " + lender.getName() + "对您的借款进行了销账!");
        params.put("keyword1", DateUtils.formateDateTime(new Date()));
        params.put("keyword2", String.format("%1$s元",
                String.valueOf(txBaseEntity.getAmount() + txBaseEntity.getInterest().doubleValue())));
        params.put("remark", "查看详情");

        wechatUtils.sendTemplateMessage(borrower.getWechatId(), confirmRepayTemplateId,
                HttpContextUtils.getRequestBaseUrl() + "/rcpt/transaction/"
                        + txBaseEntity.getTxId() + ".html", params);
    }
}
