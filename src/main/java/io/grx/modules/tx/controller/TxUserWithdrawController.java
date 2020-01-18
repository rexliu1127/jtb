package io.grx.modules.tx.controller;


import io.grx.common.annotation.SysLog;
import io.grx.common.exception.RRException;
import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.tx.dto.TxUserRewardStatVO;
import io.grx.modules.tx.dto.TxUserWithdrawalVO;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxUserWithdrawEntity;
import io.grx.modules.tx.enums.TxStatus;
import io.grx.modules.tx.enums.WithdrawalStatus;
import io.grx.modules.tx.service.TxUserWithdrawService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tx")
public class TxUserWithdrawController {
    @Autowired
    private TxUserWithdrawService txUserWithdrawService;

    /**
     * 列表
     */
    @RequestMapping("/withdrawal/list")
    @RequiresPermissions("tx:withdrawal:list")
    public R list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);

        List<TxUserWithdrawalVO> rewardStatList = txUserWithdrawService.queryAdminList(query);
        int total = txUserWithdrawService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(rewardStatList,
                total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 审核提现
     */
    @SysLog("审核提现")
    @RequestMapping("withdrawal/status")
    @RequiresPermissions("tx:withdrawal:update")
    public R updateStatus(Long id, int status) {
        WithdrawalStatus newStatus = WithdrawalStatus.valueOf(status);

        if (id != null) {
            return txUserWithdrawService.updateStatus(id, newStatus);
        }

        return R.error("非法请求");
    }
}
