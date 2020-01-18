package io.grx.wx.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.grx.common.utils.*;
import io.grx.modules.tx.entity.TxComplainEntity;
import io.grx.modules.tx.enums.ComplainResult;
import io.grx.modules.tx.enums.ComplainType;
import io.grx.modules.tx.service.*;
import io.grx.wx.converter.TxDtoConverter;
import io.grx.wx.converter.TxRepayDtoConverter;
import io.grx.wx.dto.TxDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.grx.common.exception.RRException;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.enums.TxStatus;
import io.grx.wx.annotation.WxJsSign;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rcpt")
public class TransactionController extends BaseController {
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String DEFAULT_HEAD_IMG = "../img/accoubt/photo.svg";
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

    @Autowired
    private TxComplainService txComplainService;

    @Autowired
    private TxRepayDtoConverter txRepayDtoConverter;
    @Autowired
    private TxDtoConverter txDtoConverter;

    @Autowired
    private TxUserPasswordService txUserPasswordService;

    /**
     * 打借条页面
     *
     * @return
     */
    @RequestMapping(value = "/apply_transaction.html", method = RequestMethod.GET)
    public String applyTransactionPage(Model model, HttpServletRequest request, Integer type) {
        TxUserEntity user = getUser(request);

        model.addAttribute("beginDate", LocalDate.now().format(format));
        model.addAttribute("endDate", LocalDate.now().plusDays(6).format(format));
        model.addAttribute("tx", new TxBaseEntity());
        model.addAttribute("type", type);

        if (type == 2 && txBaseService.isFreeTxLenderMobile(user.getMobile())) {
            model.addAttribute("feeAmount", BigDecimal.ZERO);
        }
        model.addAttribute("feeAmount", txBaseService.getTransactionFee());

        return TX_TEMPLATE_PATH + "apply_transaction";
    }

    /**
     * 打借条成功页面
     *
     * @return
     */
    @RequestMapping(value = "/apply_success.html", method = RequestMethod.GET)
    public String applySuccessPage(Model model, HttpServletRequest request, String txUuid) {
        TxUserEntity user = getUser(request);

        TxBaseEntity entity = txBaseService.queryByUuid(txUuid);
        model.addAttribute("tx", entity);

        model.addAttribute("baseUrl", HttpContextUtils.getRequestBaseUrl());

        return TX_TEMPLATE_PATH + "apply_success";
    }

    /**
     * 打开借条确定页面
     *
     * @return
     */
    @WxJsSign
    @RequestMapping(value = "/confirm_transaction/{txUuid}.html", method = RequestMethod.GET)
    public String confirmTransactionPage(Model model, HttpServletRequest request,  @PathVariable("txUuid") String txUuid) {
        TxUserEntity user =    getUser(request);

        TxBaseEntity entity = txBaseService.queryByUuid(txUuid);

        if (entity == null) {
            return "redirect:/rcpt/index.html";
        }

        if (entity.getStatus() != TxStatus.NEW && entity.getStatus() != TxStatus.REJECTED) {
            return "redirect:/rcpt/transaction/" + entity.getTxId() + ".html";
        }

        if (user.getUserId().equals(entity.getCreateUserId()) && entity.getStatus() == TxStatus.REJECTED) {
//        if (entity.getBorrowerUserId().equals(entity.getCreateUserId()) && entity.getStatus() == TxStatus.REJECTED) {
            // 如果再次发送
            entity.setStatus(TxStatus.NEW);
            txBaseService.update(entity);
        }

        boolean borrowerHasOverdueRecord = false;
        if (entity.getBorrowerUserId() != null) {
            borrowerHasOverdueRecord = txBaseService.hasOverdueRecord(entity.getBorrowerUserId());
        }
        model.addAttribute("tx", entity);
        model.addAttribute("borrowerHasOverdueRecord", borrowerHasOverdueRecord);
        model.addAttribute("hasPassword", txUserPasswordService.queryObject(user.getUserId()) != null);

        return TX_TEMPLATE_PATH + "confirm_transaction";
    }

    /**
     * 二维码
     */
    @RequestMapping("/confirm_transaction_code/{txUuid}.png")
    public void getConfirmTxQrCode(HttpServletResponse response, @PathVariable("txUuid") String txUuid) throws
            ServletException, IOException {
        response.setContentType("image/png");

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();

            String text = HttpContextUtils.getRequestBaseUrl() + "/rcpt/confirm_transaction/" + txUuid + ".html";
            QRCodeUtils.createQRImage(text, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 查看借条
     *
     * @return
     */
    @RequestMapping(value = "/transaction/{txId}", method = RequestMethod.GET)
    public String viewTransactionPage(Model model, HttpServletRequest request, @PathVariable("txId") Long txId) {
        TxUserEntity user = getUser(request);

        TxBaseEntity entity = txBaseService.queryObject(txId);
        if (entity == null || !(user.getUserId().equals(entity.getLenderUserId())
                || user.getUserId().equals(entity.getBorrowerUserId()))) {
            return "redirect:/rcpt/index.html";
        }
        model.addAttribute("tx", entity);

        if (user.getUserId().equals(entity.getLenderUserId())) {
            model.addAttribute("txRepay", txRepayDtoConverter.convert(entity));
        }

        return TX_TEMPLATE_PATH + "view_transaction";
    }


    /**
     * 查看借条
     *
     * @return
     */
    @RequestMapping(value = "/transaction/agreement.html", method = RequestMethod.GET)
    public String transactionAgreementPage(Model model, HttpServletRequest request, Long txId) {
        TxUserEntity user = getUser(request);

        if (txId != null) {
            TxBaseEntity entity = txBaseService.queryObject(txId);
            if (entity != null) {
                if (!StringUtils.equals(user.getName(), entity.getBorrowerName())
                        && !StringUtils.equals(user.getName(), entity.getLenderName())) {
                    throw new RRException("Invalid request", 401);
                }
                model.addAttribute("amount", entity.getAmount());
                model.addAttribute("beginDate", DateUtils.formateDate(entity.getBeginDate()));
                model.addAttribute("endDate", DateUtils.formateDate(entity.getEndDate()));
                model.addAttribute("rate", entity.getRate());
                model.addAttribute("borrowerName", entity.getBorrowerName());
                model.addAttribute("lenderName", entity.getLenderName());


                if (entity.getBorrowerUserId() != null) {
                    TxUserEntity borrower = txUserService.queryObject(entity.getBorrowerUserId());
                    model.addAttribute("borrowerId", CharUtils.maskMiddleChars(
                            borrower.getIdNo(), 6, 2, '*'));
                    model.addAttribute("borrowerMobile", CharUtils.maskMiddleChars(
                            borrower.getMobile(), 3, 4, '*'));
                }

                if (entity.getLenderUserId() != null) {
                    TxUserEntity lender = txUserService.queryObject(entity.getLenderUserId());
                    model.addAttribute("lenderId", CharUtils.maskMiddleChars(
                            lender.getIdNo(), 6, 2, '*'));
                    model.addAttribute("lenderMobile", CharUtils.maskMiddleChars(
                            lender.getMobile(), 3, 4, '*'));
                }

                model.addAttribute("createTime", DateUtils.formateDate(entity.getCreateTime()));

                model.addAttribute("usageType", entity.getUsageType().getDisplayName());
                model.addAttribute("interest", entity.getInterest());
            }
            model.addAttribute("tx", entity);
        }

        return TX_TEMPLATE_PATH + "view_agreement";
    }


    /**
     * 提交申诉页面
     *
     * @return
     */
    @WxJsSign
    @RequestMapping(value = "/submit_complain.html", method = RequestMethod.GET)
    public String complainPage(Model model, HttpServletRequest request, Long txId) {
        model.addAttribute("txId", txId);

        boolean isWechat = HttpContextUtils.isWechatClient(
                HttpContextUtils.getHttpServletRequest());
        model.addAttribute("isWechat", isWechat);

        return TX_TEMPLATE_PATH + "submit_complain";
    }

    /**
     * 提交申诉
     *
     * @return
     */
    @RequestMapping(value = "/complain", method = RequestMethod.POST)
    @ResponseBody
    public R submitComplain(Long txId, int complainType, String remark,
                               String imagePath) {
        TxUserEntity user = ShiroUtils.getTxUser();

        TxBaseEntity entity = txBaseService.queryObject(txId);

        if (entity == null || entity.getStatus() == TxStatus.NEW) {
            return R.error("借条无效");
        }

        if (!user.getUserId().equals(entity.getBorrowerUserId())) {
            throw new RRException("invalid request");
        }

        TxComplainEntity complainEntity = new TxComplainEntity();
        complainEntity.setTxId(txId);
        complainEntity.setRemark(remark);
        complainEntity.setComplainType(ComplainType.valueOf(complainType));
        complainEntity.setCreateTime(new Date());
        complainEntity.setStatus(ComplainResult.PENDING);
        complainEntity.setImagePath(imagePath);

        txComplainService.save(complainEntity);

        return R.ok();
    }
    /**
     * 借款信息（借款列表）
     */
    @RequestMapping(value = "/borrower_informationlist/{borrowerUserId}.html", method = RequestMethod.GET)
    public String information(Model model, HttpServletRequest request, @PathVariable("borrowerUserId") Long borrowerUserId) {
        List<TxDto> results=new ArrayList<TxDto>();
        List<TxBaseEntity> txList = txBaseService.queryBorrowerInformation(borrowerUserId);
        if(CollectionUtils.isNotEmpty(txList)){
            for (TxBaseEntity entity : txList) {
                TxDto dto=new TxDto();
                //重新设置姓名
                String lenderName=entity.getLenderName();
                if(StringUtils.isNotEmpty(lenderName)){
                    String lenderNameStr=lenderName.substring(0,1);
                    for(int i=1;i<lenderName.length();i++){
                        lenderNameStr=lenderNameStr+"*";
                    }
                    String newLenderName=lenderNameStr+"<i class='radius7PX'>出借人</i>";
                    dto.setLenderName(newLenderName);
                }
                dto.setTxId(entity.getTxId());
                dto.setAmount(entity.getAmount());
                dto.setRate(entity.getRate());
                dto.setBeginDate(entity.getBeginDate());
                dto.setEndDate(entity.getEndDate());
                dto.setBorrowerName(entity.getBorrowerName());
                if (entity.getBorrowerUserId() != null) {
                    TxUserEntity borrower = txUserService.queryObject(entity.getBorrowerUserId());
                    dto.setBorrowerHeadImg(borrower.getHeadImgUrl());
                } else {
                    dto.setBorrowerHeadImg(DEFAULT_HEAD_IMG);
                }
                if (entity.getLenderUserId() != null) {
                    TxUserEntity lender = txUserService.queryObject(entity.getLenderUserId());
                    dto.setLenderHeadImg(lender.getHeadImgUrl());
                } else {
                    dto.setLenderHeadImg(DEFAULT_HEAD_IMG);
                }
                dto.setTxStatus(entity.getStatus());


                if (entity.getStatus() == TxStatus.COMPLETED) {
                    dto.setRepaymentDate(entity.getRepayDate());
                }

                dto.setStatusLabel(entity.getStatus().getDisplayName());
                results.add(dto);
            }
        }
        model.addAttribute("tx", results);
        return TX_TEMPLATE_PATH + "borrower_information";
    }
}
