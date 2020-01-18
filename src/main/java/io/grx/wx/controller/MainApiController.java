package io.grx.wx.controller;

import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.grx.common.exception.RRException;
import io.grx.common.utils.CacheUtils;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxOverdueRecordService;
import io.grx.modules.tx.service.TxUserBalanceService;
import io.grx.modules.tx.service.TxUserService;
import io.grx.wx.converter.TxDtoConverter;
import io.grx.wx.dto.TxDto;

@RestController
@RequestMapping("/rcpt")
public class MainApiController extends BaseController {

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
     * 我的借条列表
     */
    @RequestMapping("/borrowed_transaction/list")
    public R listBorrowedTransaction(String lenderName){

        TxUserEntity user = ShiroUtils.getTxUser();

        List<TxDto> results = txDtoConverter.convert(txBaseService.queryBorrowedTransaction(user.getUserId(),
                lenderName));

        return R.ok().put("page", results);
    }


    /**
     * 我的借条列表
     */
    @RequestMapping("/borrowed_transaction/list_for_credit_report/{userId}")
    public R listBorrowedTransaction(@PathVariable(value="userId") Long userId, Boolean overdue, Long txId){

        TxUserEntity user = ShiroUtils.getTxUser();

        checkViewCreditAcl(user, userId, txId);

        List results = null;
        if (overdue == null || !overdue) {
            results = txDtoConverter.convert(txBaseService.queryBorrowedTransaction(userId,
                    null), true);
        } else {
            results = txOverdueRecordService.queryByUserId(userId);
        }

        return R.ok().put("page", results);
    }

    /**
     * 我的出借列表
     */
    @RequestMapping("/lended_transaction/list")
    public R listLendedTransaction(String lenderName){

        TxUserEntity user = ShiroUtils.getTxUser();
        List<TxDto> results = txDtoConverter.convert(txBaseService.queryLendedTransaction(user.getUserId(),
                lenderName));

        return R.ok().put("page", results);
    }

    /**
     * 我的出借列表
     */
    @RequestMapping("/lended_transaction/list_for_credit_report/{userId}")
    public R listLendedTransaction(@PathVariable(value="userId") Long userId, Long txId){

        TxUserEntity user = ShiroUtils.getTxUser();

        checkViewCreditAcl(user, userId, txId);
        List<TxDto> results = txDtoConverter.convert(txBaseService.queryLendedTransaction(userId,
                null), true);

        return R.ok().put("page", results);
    }

    private String creditCacheKey(Long userId) {
        return "check_credit:" + ShiroUtils.getUserId();
    }

    private void checkViewCreditAcl(TxUserEntity user, Long userId, Long txId) {
        if (txId != null) {
            TxBaseEntity txBaseEntity = txBaseService.queryObject(txId);
            if (txBaseEntity == null || !txBaseEntity.getBorrowerUserId().equals(userId)
                    || (StringUtils.isNotBlank(user.getName()) && !StringUtils.equals(txBaseEntity.getLenderName(), user
                    .getName()))) {
                throw new RRException("Invalid request 1", 401);
            }
        } else {
            String creditUserId = cacheUtils.get(creditCacheKey(ShiroUtils.getUserId()));

            if (!user.getUserId().equals(userId) && !txUserService.isFriend(user.getUserId(), userId)
                    && !StringUtils.equals(creditUserId, ObjectUtils.toString(userId))) {
                throw new RRException("Invalid request 2", 401);
            }
        }

    }
}
