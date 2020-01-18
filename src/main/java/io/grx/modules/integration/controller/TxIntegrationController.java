package io.grx.modules.integration.controller;


import io.grx.common.exception.RRException;
import io.grx.common.utils.*;
import io.grx.modules.tx.controller.TxBaseController;
import io.grx.modules.tx.converter.TxBaseVOConverter;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxLenderEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.enums.TxStatus;
import io.grx.modules.tx.enums.UsageType;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxLenderService;
import io.grx.modules.tx.service.TxUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@RestController
@RequestMapping("/integration")
public class TxIntegrationController {

    @Value("${integration.accessKey}")
    private String accessKey;
    @Value("${integration.accessSecret}")
    private String accessSecret;

    @Autowired
    private TxBaseVOConverter txBaseVOConverter;

    private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private TxUserService txUserService;

    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private TxLenderService txLenderService;

    private void checkAccessKey(String accessKey, String accessSecret) {
        if (!StringUtils.equalsIgnoreCase(accessKey, this.accessKey) || !StringUtils.equalsIgnoreCase(accessSecret, this.accessSecret)) {
            throw new RRException("accessKey不正确, 请联系客户经理", 401);
        }
    }

    /**
     * 系统打借条
     */
    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addTransaction(String accessKey, String accessSecret,
                                                String merchantNo,
                                                Integer amount, BigDecimal fee,
                                                String beginDate, String endDate,
                                                String rate,
                                                String lenderMobile,
                                                String borrowerMobile,
                                                String borrowerName,
                                                String borrowerIdNo,
                                                String borrowerBankName,
                                                String borrowerBankAccount,
                                                Integer usageType,
                                                HttpServletRequest request) {
        checkAccessKey(accessKey, accessSecret);
        if (amount == null || amount <= 0) {
            return R.error("[amount]借款金额不正确");
        }
        if (fee == null || fee.doubleValue() < 0) {
            return R.error("[fee]借条收费金额不正确");
        }

        if (StringUtils.isBlank(borrowerMobile)) {
            return R.error("[borrowerMobile]不能为空");
        }

        TxUserEntity lender = txUserService.queryByMobile(lenderMobile);
        if (lender == null) {
            return R.error("[borrowerMobile]出借人不存在");
        }

        TxLenderEntity txLenderEntity = txLenderService.queryByMobile(lenderMobile);

        if (!StringUtils.equals(merchantNo, txLenderEntity.getMerchantNo())) {
            return R.error("[merchantNo]商户号跟出借人商户不匹配");
        }

        TxUserEntity borrower = txUserService.queryByMobile(borrowerMobile);
        if (borrower != null) {
            if (StringUtils.isNotBlank(borrower.getIdNo())) {
                if (!StringUtils.equalsIgnoreCase(borrowerIdNo, borrower.getIdNo())) {
                    return R.error("借款人手机已绑定其他身份证");
                }

                if (!StringUtils.equalsIgnoreCase(borrowerName, borrower.getName())) {
                    return R.error("借款人手机已绑定其他人姓名");
                }
            } else {
                borrower.setIdNo(borrowerIdNo);
                borrower.setName(borrowerName);
                borrower.setBankAccount(borrowerBankAccount);
                borrower.setBankName(borrowerBankName);

                txUserService.update(borrower);
            }
        } else {
            borrower = new TxUserEntity();
            borrower.setIdNo(borrowerIdNo);
            borrower.setMobile(borrowerMobile);
            borrower.setName(borrowerName);
            borrower.setBankAccount(borrowerBankAccount);
            borrower.setBankName(borrowerBankName);

            txUserService.save(borrower);
        }

        int txAmount = amount;

        LocalDate beginLocalDate = null;
        LocalDate endLocalDate = null;

        try {
            beginLocalDate = LocalDate.parse(beginDate, format);
        } catch (Exception e) {
            return R.error("[beginDate]开始日期错误");
        }

        try {
            endLocalDate = LocalDate.parse(endDate, format);
        } catch (Exception e) {
            return R.error("[endDate]结束日期错误");
        }

        if (DAYS.between(beginLocalDate, endLocalDate) < 0) {
            return R.error("结束日期不能早于开始日期");
        }

        TxBaseEntity entity = new TxBaseEntity();

        String txUuid = txBaseService.getNewTxUuid();
        entity.setTxUuid(txUuid);
        entity.setAmount(txAmount);
        entity.setRate(NumberUtils.toInt(rate, 0));

        entity.setBeginDate(Date.from(beginLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        entity.setEndDate(Date.from(endLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        entity.setRemark(remark);
        entity.setUsageType(UsageType.valueOf(usageType));
//        entity.setUsageRemark(usageRemark);
        entity.setCreateUserId(lender.getUserId());
        entity.setCreateTime(new Date());

        entity.setLenderUserId(lender.getUserId());
        entity.setBorrowerName(borrowerName);
        entity.setBorrowerUserId(borrower.getUserId());
        entity.setLenderName(lender.getName());
        entity.setLenderSignImgPath(lender.getSignImgPath());
        entity.setStatus(TxStatus.NEW);

        entity.setInterest(txBaseService.calculateInterest(entity));

        BigDecimal txFee = txBaseService.getTransactionFee();
        entity.setFeeAmount(txFee);
        txBaseService.save(entity);

        String url = HttpContextUtils.getRequestBaseUrl() + "/rcpt/confirm_transaction/" + entity.getTxUuid() + ".html";

        return R.ok().put("tx", entity).put("url", url);
    }

    /**
     * 列表
     */
    @GetMapping("/lender/list")
    public R list(HttpServletRequest request) {
        Map<String, String> params = getParamsFromRequest(request);
        checkAccessKey(params.remove("accessKey"), params.remove("accessSecret"));

        //商户号
        String merchantno = params.get("merchantNo");
        params.put("_merchantNo", merchantno);

        //查询列表数据
        Query query = new Query(new HashMap<>(params));

        if (query.getLimit() == 0) {
            query.setLimit(10);
        }
        if (query.getPage() == 0) {
            query.setPage(1);
        }

        List<TxLenderEntity> txLenderList = txLenderService.queryList(query);
        int total = txLenderService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(txLenderList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 保存
     */
    @PostMapping("/lender")
    public R save(String accessKey, String accessSecret, String merchantNo, String mobile, String name, String idNo){
        checkAccessKey(accessKey, accessSecret);

        if (StringUtils.isBlank(merchantNo)) {
            return R.error("[merchantNo]商户号不能为空");
        }

        if (StringUtils.isBlank(name)) {
            return R.error("[name]姓名不能为空");
        }

        if (StringUtils.isBlank(mobile)) {
            return R.error("[mobile]手机不能为空");
        }

        if (StringUtils.isBlank(idNo)) {
            return R.error("[idNo]身份证不能为空");
        }

        if (!IdCardUtil.isIdcard(idNo)) {
            return R.error("[idNo]身份证检验错误");
        }

        TxUserEntity txuser = txUserService.queryByMobile(mobile);
        if(txuser != null) {
            if (StringUtils.isNotBlank(txuser.getName()) && !StringUtils.equals(name, txuser.getName())) {
                return R.error("该手机已绑定其他出借人!");
            }

            if (StringUtils.isNotBlank(txuser.getIdNo()) && !StringUtils.equals(idNo, txuser.getIdNo())) {
                return R.error("该身份证已绑定其他出借人!");
            }

            if (StringUtils.isBlank(txuser.getName())) {
                txuser.setName(name);
                txuser.setIdNo(idNo);
                txUserService.update(txuser);
            }
        } else {
            txuser = new TxUserEntity();
            txuser.setName(name);
            txuser.setIdNo(idNo);
            txuser.setMobile(mobile);
            txuser.setCreateTime(new Date());

            txUserService.save(txuser);
        }

        TxLenderEntity txLender = txLenderService.queryByMobile(mobile);
        if (txLender != null) {
            return R.error("该出借人已存在");
        }

        txLender = new TxLenderEntity();

        txLender.setMerchantNo(merchantNo);
        txLender.setMobile(mobile);
        txLender.setName(name);
        txLender.setStatus(1);
        txLender.setCreateTime(new Date());
        txLenderService.save(txLender);

        return R.ok();
    }


    private Map<String, String> getParamsFromRequest(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Enumeration<String> eu = request.getParameterNames();
        while (eu.hasMoreElements()) {
            String key = eu.nextElement();
            params.put(key, StringUtils.join(request.getParameterValues(key), ','));
        }
        return params;
    }


    /**
     * 列表
     */
    @RequestMapping("/tx/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        String accessKey = request.getHeader("ACCESS-KEY");
        String accessSecret = request.getHeader("ACCESS-SECRET");
        checkAccessKey(accessKey, accessSecret);

        //查询列表数据
        String merchantNo = (String) params.get("merchantNo");
        if (StringUtils.isBlank(merchantNo)) {
            return R.error("缺少参数merchantNo");
        }
        ShiroUtils.setMerchantNo(merchantNo);;

        Query query = new Query(params);

        List<String> mobiles = getLenderMobiles();
        if (CollectionUtils.isEmpty(mobiles)) {
            return R.ok().put("page", new PageUtils(Collections.EMPTY_LIST, 0, 0, 0));
        }

        query.put("lenderMobiles", getLenderMobiles());

        List<TxBaseEntity> txBaseList = txBaseService.queryList(query);
        int total = txBaseService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(txBaseVOConverter.convert(txBaseList),
                total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    private List<String> getLenderMobiles() {
        return txLenderService.getAllLenderMobiles();
    }
}
