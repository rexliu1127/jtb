package io.grx.wx.controller;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.grx.common.utils.CacheUtils;
import io.grx.modules.tx.entity.TxUserBalanceEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxOverdueRecordService;
import io.grx.modules.tx.service.TxUserBalanceService;
import io.grx.modules.tx.service.TxUserService;
import io.grx.wx.converter.TxDtoConverter;

@Controller
@RequestMapping("/rcpt")
public class MainController extends BaseController {
    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private TxUserService txUserService;

    @Autowired
    private TxOverdueRecordService txOverdueRecordService;

    @Autowired
    private TxDtoConverter txDtoConverter;
    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private TxUserBalanceService txUserBalanceService;

    /**
     * 测试hello
     *
     * @return
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String main(Model model, HttpServletRequest request) {
        TxUserEntity user =   getUser(request);
        model.addAttribute("user", user);

        if (StringUtils.isBlank(user.getName())) {
            // 未实名认证
            model.addAttribute("borrowingList", Collections.EMPTY_LIST);
            model.addAttribute("lendingList", Collections.EMPTY_LIST);

            model.addAttribute("sumPendingRepayByMe", 0);
            model.addAttribute("sumPendingRepayToMe", 0);
            model.addAttribute("balance", 0.0);
        } else {
            model.addAttribute("borrowingList", txDtoConverter.convert(txBaseService.getPendingToConfirmBorrowingList(user
                    .getUserId())));
            model.addAttribute("lendingList", txDtoConverter.convert(txBaseService.getPendingToConfirmLendingList(user
                    .getUserId())));

            model.addAttribute("sumPendingRepayByMe", txBaseService.sumPendingRepayByMe(user.getUserId()));
            model.addAttribute("sumPendingRepayToMe", txBaseService.sumPendingRepayToMe(user.getUserId()));

            TxUserBalanceEntity userBalanceEntity = txUserBalanceService.queryObject(user.getUserId());
            model.addAttribute("balance", userBalanceEntity != null ? userBalanceEntity.getBalance().doubleValue() :
                    0.0);
        }

        return TX_TEMPLATE_PATH + "index";
    }

    /**
     * 我的借条
     *
     * @return
     */
    @RequestMapping(value = "/borrower_sum.html", method = RequestMethod.GET)
    public String borrowerSummary(Model model, HttpServletRequest request, String searchValue) {
        TxUserEntity user = getUser(request);
        model.addAttribute("user", user);

        model.addAttribute("searchValue", searchValue);

        model.addAttribute("sumBorrow", txBaseService.sumBorrow(user.getUserId()));
        model.addAttribute("sumBorrowCount", txBaseService.sumBorrowCount(user.getUserId()));

        model.addAttribute("sumBorrowInterest", txBaseService.sumBorrowInterest(user.getUserId()));

        return TX_TEMPLATE_PATH + "borrower_sum";
    }

    /**
     * 我的出借页面
     *
     * @return
     */
    @RequestMapping(value = "/lender_sum.html", method = RequestMethod.GET)
    public String lenderSummary(Model model, HttpServletRequest request, String searchValue) {
        TxUserEntity user = getUser(request);
        model.addAttribute("user", user);

        model.addAttribute("searchValue", searchValue);

        model.addAttribute("sumLendedWithoutInterest", txBaseService.sumLendedWithoutInterest(user.getUserId()));
        model.addAttribute("sumLendedCount", txBaseService.sumLendedCount(user.getUserId()));

        model.addAttribute("sumLendedInterest", txBaseService.sumLendedInterest(user.getUserId()));

        return TX_TEMPLATE_PATH + "lender_sum";
    }

    /**
     * 测试hello
     *
     * @return
     */
    @RequestMapping(value = "/notice", method = RequestMethod.GET)
    public String noticePage(Model model, String type, String title, String message, String returnUrl) {
        model.addAttribute("type", "type");
        model.addAttribute("title", "title");
        model.addAttribute("message", "message");
        model.addAttribute("returnUrl", "returnUrl");

        return TX_TEMPLATE_PATH + "notice";
    }
}
