package io.grx.wx.controller;

import java.util.List;
import java.util.Map;

import io.grx.modules.tx.entity.TxUserPasswordEntity;
import io.grx.modules.tx.service.TxComplainService;
import io.grx.modules.tx.service.TxUserPasswordService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.grx.common.exception.RRException;
import io.grx.common.utils.Query;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.wx.converter.TxRepaymentDtoConverter;
import io.grx.wx.dto.TxRepayDto;
import io.grx.common.utils.R;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.service.TxRepaymentService;
import io.grx.wx.converter.TxDtoConverter;
import io.grx.wx.converter.TxRepayDtoConverter;

@Controller
@RequestMapping("/rcpt")
public class RepaymentListController extends BaseController {
    @Autowired
    private TxBaseService txBaseService;
    @Autowired
    private TxRepayDtoConverter txRepayDtoConverter;

    @Autowired
    private TxDtoConverter txDtoConverter;

    @Autowired
    private TxRepaymentService txRepaymentService;

    @Autowired
    private TxRepaymentDtoConverter txRepaymentDtoConverter;

    @Autowired
    private TxComplainService txComplainService;

    @Autowired
    private TxUserPasswordService txUserPasswordService;

    /**
     * 待还款页面
     *
     * @return
     */
    @RequestMapping(value = "/repayment_to_pay", method = RequestMethod.GET)
    public String pendingToPayPage(Model model, String searchValue) {
        TxUserEntity user = ShiroUtils.getTxUser();

        model.addAttribute("searchValue", searchValue);

        model.addAttribute("allPendingToPay", txBaseService.sumPendingToPayWithInterest(user.getUserId(),
                -1));
        model.addAttribute("sevenDaysPendingToPay", txBaseService.sumPendingToPayWithInterest(user.getUserId(),
                7));
        model.addAttribute("thirtyDaysPendingToPay", txBaseService.sumPendingToPayWithInterest(user.getUserId(),
                30));

        return TX_TEMPLATE_PATH + "repayment_to_pay";
    }

    /**
     * 待还款列表数据
     */
    @RequestMapping("/repayment/to_pay_list")
    @ResponseBody
    public R listRepaymentToPay(@RequestParam Map<String, Object> params){

        TxUserEntity user = ShiroUtils.getTxUser();

        Query query = new Query(params);
        query.put("userId", user.getUserId());
        List<TxRepayDto> results = txRepayDtoConverter.convert(txBaseService.queryPendingToPayList(query));

        return R.ok().put("page", results);
    }


    /**
     * 待收款页面
     *
     * @return
     */
    @RequestMapping(value = "/repayment_repay", method = RequestMethod.GET)
    public String pendingRepayPage(Model model, String searchValue,
                                   @RequestParam(value = "last", defaultValue = "0") int last) {
        TxUserEntity user = ShiroUtils.getTxUser();

        model.addAttribute("searchValue", searchValue);

        model.addAttribute("allPendingRepay", txBaseService.sumPendingRepayWithInterest(user.getUserId(),
                -1));
        model.addAttribute("sevenDaysPendingRepay", txBaseService.sumPendingRepayWithInterest(user.getUserId(),
                7));
        model.addAttribute("thirtyDaysPendingRepay", txBaseService.sumPendingRepayWithInterest(user.getUserId(),
                30));

        return TX_TEMPLATE_PATH + "repayment_repay";
    }

    /**
     * 待收款页面
     *
     * @return
     */
    @RequestMapping(value = "/repayment_repay_plan", method = RequestMethod.GET)
    public String pendingRepayPlanPage(Model model) {
        TxUserEntity user = ShiroUtils.getTxUser();


        model.addAttribute("sumPendingRepay", txBaseService.sumPendingRepayWithInterest(user.getUserId(), -1));
        model.addAttribute("sumRepaid", txBaseService.sumRepaid(user.getUserId()));
//        model.addAttribute("borrowingList", txRepayDtoConverter.convert(txBaseService.getLendingList(user
//                .getUserId())));
//        model.addAttribute("repaymentList", txRepaymentDtoConverter.convert(txRepaymentService.queryListByLender(user
//                .getUserId())));

        return TX_TEMPLATE_PATH + "repayment_repay_plan";
    }

    /**
     * 待收款页面
     *
     * @return
     */
    @RequestMapping(value = "/repayment_repay_plan/{txId}", method = RequestMethod.GET)
    public String pendingRepayPlanDetail(Model model, @PathVariable(value="txId") Long txId, HttpServletRequest request) {
        TxUserEntity user = ShiroUtils.getTxUser();

        TxUserPasswordEntity txUserPasswordEntity = txUserPasswordService.queryObject(user.getUserId());
        if (txUserPasswordEntity == null) {
            return "redirect:/rcpt/set_password.html?returnUrl=" + StringUtils.substringAfter(request.getRequestURI(),
                    request.getContextPath());
        }

        TxBaseEntity txBaseEntity = txBaseService.queryObject(txId);
        if (txBaseEntity == null || !user.getUserId().equals(txBaseEntity.getLenderUserId())) {
            throw new RRException("invalid request");
        }

        model.addAttribute("txRepay", txRepayDtoConverter.convert(txBaseEntity));

        model.addAttribute("repaymentList", txRepaymentDtoConverter.convert(txRepaymentService.queryListByTx(txId)));

        return TX_TEMPLATE_PATH + "repayment_repay_plan";
    }

    /**
     * 待收款页面
     *
     * @return
     */
    @RequestMapping(value = "/repayment_repay_plan/data_list", method = RequestMethod.GET)
    @ResponseBody
    public R getDataListForRepaymentRepayPlan(String userName) {
        TxUserEntity user = ShiroUtils.getTxUser();

        return R.ok().put("borrowingList", txRepayDtoConverter.convert(txBaseService.getLendingList(user
                .getUserId(), userName)))
                .put("repaymentList", txRepaymentDtoConverter.convert(txRepaymentService.queryListByLender(user
                        .getUserId(), userName)));
    }


    /**
     * 待还款详情页面
     *
     * @return
     */
    @RequestMapping(value = "/repayment_to_pay_plan/{txId}.html", method = RequestMethod.GET)
    public String pendingToPayPlanDetail(Model model, @PathVariable(value="txId") Long txId) {
        TxUserEntity user = ShiroUtils.getTxUser();

        TxBaseEntity txBaseEntity = txBaseService.queryObject(txId);
        if (txBaseEntity == null || !user.getUserId().equals(txBaseEntity.getBorrowerUserId())) {
            throw new RRException("invalid request");
        }

        model.addAttribute("txRepay", txRepayDtoConverter.convert(txBaseEntity));

        model.addAttribute("repaymentList", txRepaymentDtoConverter.convert(txRepaymentService.queryListByTx(txId)));

        model.addAttribute("txComplain", txComplainService.getLatestByTxId(txId));

        return TX_TEMPLATE_PATH + "repayment_to_pay_plan";
    }

    /**
     * 待收款页面
     *
     * @return
     */
    @RequestMapping(value = "/repayment_to_pay_plan/data_list", method = RequestMethod.GET)
    @ResponseBody
    public R getDataListForRepaymentToPayPlan(String userName) {
        TxUserEntity user = ShiroUtils.getTxUser();
        return R.ok().put("lendingList", txRepayDtoConverter.convert(txBaseService.getBorrowingList(user
                .getUserId(), userName)))
                .put("repaymentList", txRepaymentDtoConverter.convert(txRepaymentService.queryListByBorrower(user
                        .getUserId(), userName)));
    }


    /**
     * 待收款页面
     *
     * @return
     */
    @RequestMapping(value = "/repayment_to_pay_plan", method = RequestMethod.GET)
    public String pendingToPayPlanPage(Model model) {
        TxUserEntity user = ShiroUtils.getTxUser();

        model.addAttribute("sumPendingToPay", txBaseService.sumPendingToPayWithInterest(user.getUserId(), -1));
        model.addAttribute("sumPaid", txBaseService.sumPaid(user.getUserId()));
//        model.addAttribute("lendingList", txRepayDtoConverter.convert(txBaseService.getBorrowingList(user
//                .getUserId())));
//        model.addAttribute("repaymentList", txRepaymentDtoConverter.convert(txRepaymentService.queryListByBorrower(user
//                .getUserId())));

        return TX_TEMPLATE_PATH + "repayment_to_pay_plan";
    }

    /**
     * 待还款列表数据
     */
    @RequestMapping("/repayment/repay_list")
    @ResponseBody
    public R listRepaymentRepay(@RequestParam Map<String, Object> params){

        TxUserEntity user = ShiroUtils.getTxUser();

        Query query = new Query(params);
        query.put("userId", user.getUserId());
        List<TxRepayDto> results = txRepayDtoConverter.convert(txBaseService.queryPendingRepayList(query));

        return R.ok().put("page", results);
    }
}
