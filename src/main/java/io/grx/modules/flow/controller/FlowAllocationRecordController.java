package io.grx.modules.flow.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.flow.service.FlowAllocationRecordService;
import io.grx.modules.sys.controller.AbstractController;

@RestController
@RequestMapping("/flowAllocationRecord")
public class FlowAllocationRecordController extends AbstractController {

	@Autowired
	private FlowAllocationRecordService flowAllocationRecordService;
	
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
        List<Map<String, Object>> list = flowAllocationRecordService.queryAllocationStatis(query);
        Integer total = flowAllocationRecordService.queryAllocationStatisTotal(query);
		PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
		return R.ok().put("page", pageUtil);
	}
	
}
