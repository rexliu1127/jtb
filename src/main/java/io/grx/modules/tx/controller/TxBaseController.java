package io.grx.modules.tx.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import io.grx.common.exception.RRException;
import io.grx.common.utils.*;
import io.grx.modules.sys.service.SysConfigService;
import io.grx.modules.tx.dto.TxBaseSummary;
import io.grx.modules.tx.dto.TxMonthSummary;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.service.TxLenderService;
import io.grx.modules.tx.service.TxUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.common.annotation.SysLog;
import io.grx.modules.tx.converter.TxBaseVOConverter;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.enums.TxStatus;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxMessageService;
import io.grx.wx.dto.TxBorrowerSummary;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


/**
 * 交易基本资料
 *
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-11-26 22:41:25
 */
@RestController
@RequestMapping("/tx/txbase")
public class TxBaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private TxBaseVOConverter txBaseVOConverter;

    @Autowired
    private TxMessageService txMessageService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private TxUserService txUserService;

    @Autowired
    private TxLenderService txLenderService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("tx:txbase:list")
    public R list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);

        if (!StringUtils.equals(Constant.DEFAULT_MERCHANT_NO, ShiroUtils.getMerchantNo())) {

            List<String> mobiles = getLenderMobiles();
            if (CollectionUtils.isEmpty(mobiles)) {
                return R.ok().put("page", new PageUtils(Collections.EMPTY_LIST, 0, 0, 0));
            }

            query.put("lenderMobiles", getLenderMobiles());
        }

        List<TxBaseEntity> txBaseList = txBaseService.queryList(query);
        int total = txBaseService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(txBaseVOConverter.convert(txBaseList),
                total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{txId}")
    @RequiresPermissions("tx:txbase:info")
    public R info(@PathVariable("txId") Long txId){
        TxBaseEntity txBase = txBaseService.queryObject(txId);
        checkAdminACL(txBase);

        return R.ok().put("txBase", txBase);
    }

    /**
     * 信息
     */
    @RequestMapping("/borrower_sum")
    public R borrowerSum(String idNo, String mobile){
        TxBorrowerSummary data = txBaseService.getBorrowerSummary(idNo, mobile);

        return R.ok().put("data", data);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("tx:txbase:save")
    public R save(@RequestBody TxBaseEntity txBase){
        checkAdminACL(txBase);
        txBaseService.save(txBase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("tx:txbase:update")
    public R update(@RequestBody TxBaseEntity txBase){
        checkAdminACL(txBase);
        txBaseService.update(txBase);

        return R.ok();
    }


    /**
     * 标记未还款
     */
    @SysLog("更改借条状态")
    @RequestMapping("/status/{txStatus}")
    @RequiresPermissions("tx:txbase:update")
    public R markAs(@RequestBody Long[] txIds, @PathVariable(value = "txStatus") String statusValue) {
        TxStatus newStatus = TxStatus.valueOf(statusValue);
        if (txIds != null) {
            if (newStatus == TxStatus.CONFIRMED) {
                for (Long txId : txIds) {
                    TxBaseEntity entity = txBaseService.queryObjectNoAcl(txId);
                    checkAdminACL(entity);

                    if (entity.getStatus() == TxStatus.COMPLETED) {
                        entity.setRepayDate(null);

                        if (entity.getEndDate().getTime()
                                >= Date.from(LocalDate.now()
                                .atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()) {
                            entity.setStatus(TxStatus.CONFIRMED);
                        } else {
                            entity.setStatus(TxStatus.DELAYED);
                        }

                        txBaseService.update(entity);
                    }
                }
            }
        }

        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("刪除借條")
    @RequestMapping("/delete")
    @RequiresPermissions("tx:txbase:delete")
    public R delete(@RequestBody Long[] txIds){
        txBaseService.deleteBatch(txIds);

        return R.ok();
    }

    /**
     *  打借条推广二维码
     */
    @RequestMapping("/qrCode/{txUuid}.png")
    public void getQRCode(HttpServletResponse response, @PathVariable("txUuid") String txUuid) throws ServletException, IOException {
        response.setContentType("image/png");

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();

            String text = HttpContextUtils.getRequestBaseUrl() + "/rcpt/confirm_transaction/" + txUuid+".html";
            QRCodeUtils.createQRImage(text, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 销条
     */
    @SysLog("销条")
    @RequestMapping("/complete")
    @RequiresPermissions("tx:txbase:update")
    public R complete(@RequestBody Long[] txIds){
        if (txIds != null) {
            for (Long txId : txIds) {
                TxBaseEntity entity = txBaseService.queryObjectNoAcl(txId);
                checkAdminACL(entity);

                if (entity.getStatus() == TxStatus.DELAYED || entity.getStatus() == TxStatus.CONFIRMED) {
                    entity.setRepayDate(new Date());
                    entity.setStatus(TxStatus.COMPLETED);

                    txBaseService.update(entity);

                    try {
                        txMessageService.sendMsgForLenderCompleteTx(entity);
                    } catch (Throwable t) {
                        logger.error("Error in sending wechat message", t);
                    }
                }
            }
        }

        return R.ok();
    }

    private List<String> getLenderMobiles() {
        return txLenderService.getAllLenderMobiles();
    }

    private void checkAdminACL(TxBaseEntity txBaseEntity) {
        if (txBaseEntity == null) {
            return;
        }
        if (StringUtils.equals(ShiroUtils.getMerchantNo(), Constant.DEFAULT_MERCHANT_NO)) {
            return;
        }

        if (txBaseEntity.getLenderUserId() == null) {
            throw new RRException("Invalid request");
        }
        TxUserEntity txUserEntity = txUserService.queryObject(txBaseEntity.getLenderUserId());
        List<String> lenderMobiles = getLenderMobiles();
        if (CollectionUtils.isEmpty(lenderMobiles) || !lenderMobiles.contains(txUserEntity.getMobile())) {
            throw new RRException("Invalid request");
        }
    }


    /**
     * 列表
     */
    @RequestMapping("/sum/today")
    @RequiresPermissions("tx:txbase:list")
    public R getTodaySummary(){

        //查询列表数据
        Query query = new Query(new HashMap<>());

        if (!StringUtils.equals(Constant.DEFAULT_MERCHANT_NO, ShiroUtils.getMerchantNo())) {

            List<String> mobiles = getLenderMobiles();
            if (CollectionUtils.isEmpty(mobiles)) {
                return R.ok().put("sum", new TxBaseSummary());
            }

            query.put("lenderMobiles", getLenderMobiles());
        }

        query.put("today", DateUtils.formateDate(new Date()));

        return R.ok().put("sum", txBaseService.queryTodaySummary(query));
    }


    /**
     * 列表
     */
    @RequestMapping("/sum/history")
    @RequiresPermissions("tx:txbase:list")
    public R getHistorySummary(@RequestParam Map<String, Object> params){

        //查询列表数据
        Query query = new Query(new HashMap<>());

        if (!StringUtils.equals(Constant.DEFAULT_MERCHANT_NO, ShiroUtils.getMerchantNo())) {

            List<String> mobiles = getLenderMobiles();
            if (CollectionUtils.isEmpty(mobiles)) {
                return R.ok().put("sum", new TxBaseSummary());
            }

            query.put("lenderMobiles", getLenderMobiles());
        }

        return R.ok().put("sum", txBaseService.queryHistorySummary(query));
    }


    /**
     * 列表
     */
    @RequestMapping("/sum/month")
    @RequiresPermissions("tx:txbase:list")
    public R getMonthSummary(@RequestParam Map<String, Object> params){

        //查询列表数据
        Query query = new Query(new HashMap<>());

        if (!StringUtils.equals(Constant.DEFAULT_MERCHANT_NO, ShiroUtils.getMerchantNo())) {

            List<String> mobiles = getLenderMobiles();
            if (CollectionUtils.isEmpty(mobiles)) {
                return R.ok().put("sum", new TxBaseSummary());
            }

            query.put("lenderMobiles", getLenderMobiles());
        }

        List<TxMonthSummary> results = txBaseService.queryMonthSummary(query);
        if (results.size() == 1) {
            TxMonthSummary first = new TxMonthSummary();
            first.setBorrowedAmount(BigDecimal.ZERO);
            first.setRepaidAmount(BigDecimal.ZERO);

            results.add(0, first);

            LocalDate lastMonth = LocalDate.now().minusMonths(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            first.setPeriod(lastMonth.format(formatter));
        }

        return R.ok().put("sum", results);
    }
}
