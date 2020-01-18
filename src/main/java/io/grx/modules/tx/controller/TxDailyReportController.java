package io.grx.modules.tx.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;
import io.grx.modules.tx.entity.TxDailyReportEntity;
import io.grx.modules.tx.service.TxDailyReportService;




/**
 * 每日借条统计
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-01-27 22:50:18
 */
@Controller
@RequestMapping("wx/tx/txdailyreport")
public class TxDailyReportController {
	@Autowired
	private TxDailyReportService txDailyReportService;

	@RequestMapping("/page")
	public String listPage(Model model, @RequestParam Map<String, Object> params) {
		Query query = new Query(params);
		List<TxDailyReportEntity> txDailyReportList = txDailyReportService.queryList(query);
		model.addAttribute("list", txDailyReportList);
		model.addAttribute("sum", txDailyReportService.queryStatSum());
		return "report/tx_stat";
	}

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<TxDailyReportEntity> txDailyReportList = txDailyReportService.queryList(query);
		int total = txDailyReportService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(txDailyReportList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
}
