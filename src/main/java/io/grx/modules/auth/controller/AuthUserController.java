package io.grx.modules.auth.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.grx.common.utils.*;
import io.grx.modules.auth.converter.AuthUserVOConverter;
import io.grx.modules.auth.dto.AuthRequestHistoryVO;
import io.grx.modules.auth.dto.AuthRequestVO;
import io.grx.modules.auth.dto.AuthUserVO;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.service.SysUserChannelService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.modules.auth.dto.AuthUserStatVO;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.service.AuthUserService;

import javax.servlet.http.HttpServletResponse;


/**
 * 认证用户
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:19
 */
@RestController
@RequestMapping("/autha")
public class AuthUserController {
	@Autowired
	private AuthUserService authUserService;

	@Autowired
    private AuthRequestService authRequestService;

	@Autowired
	private AuthUserVOConverter authUserVOConverter;

	@Autowired
    private PoiUtils poiUtils;

	@Autowired
	private SysUserChannelService sysUserChannelService;

	/**
	 * 列表
	 */
	@RequestMapping("/user/list")
	@RequiresPermissions("auth:user:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		SysUserEntity user = ShiroUtils.getUserEntity();
		if (user != null && user.isChannelUser()) {
			query.put("_isChannelUser", true);
			query.put("_channelIdList", sysUserChannelService.queryUserChannelIdList(user.getUserId()));
		}

		List<AuthUserVO> authRequestList = authUserVOConverter.convert(authUserService.queryList(query));

		int total = authUserService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(authRequestList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

    /**
     * 列表
     */
    @RequestMapping("/user/export")
    @RequiresPermissions("auth:user:export")
    public void exportList(@RequestParam Map<String, Object> params, HttpServletResponse response){
        //查询列表数据
        Query query = new Query(params);

        List<AuthUserVO> authUserVOList = authUserVOConverter.convert(authUserService.queryList(query));

        exportAuthUsers(authUserVOList, response);
    }

    private void exportAuthUsers(List<AuthUserVO> requestVOList, HttpServletResponse response) {
        String filename = "auth_user_list_" + System.currentTimeMillis();

        // Set the content type and attachment header.
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/msexcel");// 设置为下载application/x-download
        response.setHeader("Content-Disposition", "inline;filename=\""
                + filename + ".xls\"");

        List<Object[]> dataList = new ArrayList<>();
        Object[] header = new Object[] {"ID", "注册时间", "姓名", "手机", "身份证号", "性别", "年龄", "芝麻分", "户籍城市", "公司ID", "渠道名称", "资料认证", "借条平台授权"};
        dataList.add(header);

        for (AuthUserVO requestVO : requestVOList) {
            Object[] row = new String[] {
                    String.valueOf(requestVO.getUserId()),
                    DateUtils.formateDateTime(requestVO.getCreateTime()),
                    requestVO.getName(),
                    requestVO.getMobile(),
                    requestVO.getIdNo(),
                    requestVO.getSex(),
                    String.valueOf(requestVO.getAge()),
                    String.valueOf(requestVO.getZhimaPoint()),
                    requestVO.getCity(),
                    requestVO.getMerchantNo(),
                    requestVO.getChannelName(),
                    StringUtils.join(requestVO.getBasicAuthNames(), ","),
                    StringUtils.join(requestVO.getLoanAuthNames(), ",")
            };
            dataList.add(row);
        }

        try {
            poiUtils.createExcel("客户列表", dataList, response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 列表
	 */
	@RequestMapping("/auditor/stat")
	@RequiresPermissions("stat:authuser")
	public R listAuditorStat(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
        query.put("userType", 0);

		List<AuthUserStatVO> authUserList = authUserService.queryStatList(query);
		int total = authUserService.queryStatTotal(query);

		PageUtils pageUtil = new PageUtils(authUserList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 列表
	 */
	@RequestMapping("/request/stat")
	@RequiresPermissions("stat:authuser")
	public R listRequestStat(){
		//查询列表数据
		return R.ok().put("stat", authUserService.queryRequestStat());
	}

    /**
     * 列表
     */
    @RequestMapping("/assignee/stat")
    @RequiresPermissions("stat:authuser")
    public R listAssigneeStat(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);
        query.put("userType", 1);

        List<AuthUserStatVO> authUserList = authUserService.queryStatList(query);
        int total = authUserService.queryStatTotal(query);

        PageUtils pageUtil = new PageUtils(authUserList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("auth:authuser:save")
	public R save(@RequestBody AuthUserEntity authUser){
		authUserService.save(authUser);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("auth:authuser:update")
	public R update(@RequestBody AuthUserEntity authUser){
		authUserService.update(authUser);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("auth:authuser:delete")
	public R delete(@RequestBody Long[] userIds){
		authUserService.deleteBatch(userIds);
		
		return R.ok();
	}

    /**
     * 信息
     */
    @RequestMapping("/user/info/{userId}")
    @RequiresPermissions("auth:user:info")
    public R info(@PathVariable("userId") Long userId){
        AuthUserEntity authUser = authUserService.queryObject(userId);

        if (authUser == null) {
            return R.error();
        }

        if (!StringUtils.equals(ShiroUtils.getMerchantNo(), Constant.DEFAULT_MERCHANT_NO)
                && !StringUtils.equals(ShiroUtils.getMerchantNo(), authUser.getMerchantNo())) {
            return R.error(401, "invalid request");
        }

        AuthRequestEntity authRequest = authRequestService.queryLatestByUserId(userId, null);

        int count = 0;

        if (authRequest != null) {
            count = authRequestService.queryRequestCount(authUser.getUserId(), authRequest.getRequestId());
        }

        return R.ok().put("request", authRequest)
                .put("user", authUser)
                .put("count", count);
    }
}
