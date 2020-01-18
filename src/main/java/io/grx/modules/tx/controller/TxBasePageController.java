package io.grx.modules.tx.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.pdf.BaseFont;
import io.grx.common.exception.RRException;
import io.grx.common.utils.*;
import io.grx.modules.auth.dto.AuthRequestVO;
import io.grx.modules.sys.service.SysConfigService;
import io.grx.modules.tx.service.TxLenderService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.enums.TxStatus;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxUserService;
import org.springframework.web.bind.annotation.RequestParam;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

@Controller
@RequestMapping("/tx/txbase")
public class TxBasePageController {
    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private TxUserService txUserService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private TxLenderService txLenderService;

    /**
     * 查看借条
     *
     * @return
     */
    @RequestMapping(value = "/agreement", method = RequestMethod.GET)
    @RequiresPermissions("tx:txbase:list")
    public String transactionAgreementPage(Model model, HttpServletRequest request, Long txId) {

        if (txId != null) {
            TxBaseEntity entity = txBaseService.queryObjectNoAcl(txId);

            if (entity == null) {
                throw new RRException("Invalid request");
            }

            if (!StringUtils.equals(Constant.DEFAULT_MERCHANT_NO, ShiroUtils.getMerchantNo())) {

                List<String> mobiles = getLenderMobiles();

                TxUserEntity lender = txUserService.queryObject(entity.getLenderUserId());

                if (!mobiles.contains(lender.getMobile())) {
                    throw new RRException("Invalid request");
                }
            }

            if (entity != null && entity.getStatus() != TxStatus.NEW && entity.getStatus() != TxStatus.REJECTED
                    && entity.getStatus() != TxStatus.UNPAID) {
                model.addAttribute("amount", entity.getAmount());
                model.addAttribute("beginDate", DateUtils.formateDate(entity.getBeginDate()));
                model.addAttribute("endDate", DateUtils.formateDate(entity.getEndDate()));
                model.addAttribute("rate", entity.getRate());
                model.addAttribute("borrowerName", entity.getBorrowerName());
                model.addAttribute("lenderName", entity.getLenderName());

                TxUserEntity borrower = txUserService.queryObject(entity.getBorrowerUserId());
                model.addAttribute("borrowerId", borrower.getIdNo());
                model.addAttribute("borrowerMobile", borrower.getMobile());
                model.addAttribute("borrower", borrower);

                TxUserEntity lender = txUserService.queryObject(entity.getLenderUserId());
                model.addAttribute("lender", lender);
                model.addAttribute("lenderId", lender.getIdNo());
                model.addAttribute("lenderMobile", lender.getMobile());
                model.addAttribute("createTime", DateUtils.formateDate(entity.getCreateTime()));

                model.addAttribute("usageType", entity.getUsageType().getDisplayName());
                model.addAttribute("interest", entity.getInterest());
            }
            model.addAttribute("tx", entity);
        }

        return "rcpt/view_agreement";
    }

    private List<String> getLenderMobiles() {
        return txLenderService.getAllLenderMobiles();
    }

    /**
     * 列表
     */
    @RequestMapping("/download_agreement")
    public void exportList(Long txId, String token, HttpServletResponse response) throws Exception {
        String filename = "agreement_" + txId + "_" + System.currentTimeMillis();

        // Set the content type and attachment header.
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/pdf");// 设置为下载application/x-download
        response.setHeader("Content-Disposition", "inline;filename=\""
                + filename + ".pdf\"");

        convertHtmlToPdf(HttpContextUtils.getRequestBaseUrl() + "/tx/txbase/agreement?txId=" + txId + "&token=" + token,
                response.getOutputStream());

    }

    public static boolean convertHtmlToPdf(String url, OutputStream os)
            throws Exception {

        ITextRenderer renderer = new ITextRenderer();

        HttpResponse response = HttpUtils.doGet(url, MapUtils.EMPTY_MAP);
        String html = EntityUtils.toString(response.getEntity());
        renderer.setDocumentFromString(html);

//        // 解决中文支持问题
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont("config/simsun.ttc",BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);// 宋体字
        //解决图片的相对路径问题，绝对路径不需要写
//        renderer.getSharedContext().setBaseURL("file:/D:/");
        renderer.layout();
        renderer.createPDF(os);

        return true;
    }
}
