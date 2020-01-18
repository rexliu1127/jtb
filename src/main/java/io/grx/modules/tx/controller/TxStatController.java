package io.grx.modules.tx.controller;

import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;
import io.grx.modules.tx.dto.TxUserRewardStatVO;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.service.TxUserRewardService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tx")
public class TxStatController {
    @Autowired
    private TxUserRewardService txUserRewardService;
    /**
     * 列表
     */
    @RequestMapping("/reward/stat")
    @RequiresPermissions("stat:tx_user_reward:list")
    public R list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);

        List<TxUserRewardStatVO> rewardStatList = txUserRewardService.queryRewardStat(query);
        int total = txUserRewardService.queryRewardStatTotal(query);

        PageUtils pageUtil = new PageUtils(rewardStatList,
                total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 列表
     */
    @RequestMapping("/reward/sum")
    @RequiresPermissions("stat:tx_user_reward:list")
    public R getRewardSum(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);

        return R.ok().put("stat", txUserRewardService.queryRewardSum());
    }
}
