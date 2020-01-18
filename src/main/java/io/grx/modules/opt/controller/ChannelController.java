package io.grx.modules.opt.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.grx.common.utils.*;
import io.grx.modules.opt.dto.ChannelStatVO;
import io.grx.modules.sys.entity.SysChannelUserEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.reflection.ArrayUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.grx.common.annotation.SysLog;
import io.grx.common.exception.RRException;
import io.grx.common.validator.ValidatorUtils;
import io.grx.modules.auth.converter.AuthRequestVOConverter;
import io.grx.modules.auth.dto.AuthRequestVO;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.opt.entity.ChannelEntity;
import io.grx.modules.opt.service.ChannelService;
import io.grx.modules.oss.cloud.CloudStorageService;
import io.grx.modules.oss.cloud.OSSFactory;
import io.grx.modules.sys.controller.AbstractController;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.service.SysUserChannelService;
import io.grx.modules.sys.service.SysChannelUserService;
import io.grx.modules.sys.service.SysConfigService;
import io.grx.modules.sys.service.SysUserService;


/**
 * 渠道
 *
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-11-22 20:19:17
 */
@RestController
@RequestMapping("/opt/channel")
public class ChannelController extends AbstractController {
    @Autowired
    private ChannelService channelService;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserChannelService sysUserChannelService;

    @Autowired
    private AuthRequestService authRequestService;

    @Autowired
    private AuthRequestVOConverter authRequestVOConverter;

    private CloudStorageService cloudStorageService;

    @Autowired
    private SysChannelUserService sysChannelUserService;
    
    @Autowired
    private SysConfigService sysConfigService;

    @Value("${upload.path}")
    private String fileDirectory;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = {"opt:channel:list", "opt:channel:order_list"}, logical = Logical.OR)
    public R list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);

        SysUserEntity user = getUser();

        if (user != null && user.isChannelUser()) {
            query.put("_isChannelUser", true);
            query.put("_channelIdList", sysUserChannelService.queryUserChannelIdList(user.getUserId()));
        }

        List<ChannelEntity> channelList = channelService.queryList(query);
        //查询每个渠道的审核人
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(channelList)){
            for (ChannelEntity channelEntity : channelList){
                // 获取渠道拥有审核员列表
                List<SysChannelUserEntity> auditorIdAndNameList = sysChannelUserService.queryChannelUserNameIdList(channelEntity.getChannelId());
                if(org.apache.commons.collections.CollectionUtils.isNotEmpty(auditorIdAndNameList)){
                    //查询渠道有多个审核人时只显示第一个其余的用省略号，如果是一个则只显示一个
                    if(auditorIdAndNameList.size()>1){
                        String userNameStr=auditorIdAndNameList.get(0).getUserName()+"...";
                        channelEntity.setAuditorUserName(userNameStr);
                    }else{
                        String userNameStr=auditorIdAndNameList.get(0).getUserName();
                        channelEntity.setAuditorUserName(userNameStr);
                    }
                }
            }
        }
        int total = channelService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(channelList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 列表
     */
    @RequestMapping("/order_list")
    @RequiresPermissions(value = {"opt:channel:order_list"}, logical = Logical.OR)
    public R listChannelAuthReuqest(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);

        SysUserEntity user = getUser();

        query.put("_channelIdList", sysUserChannelService.queryUserChannelIdList(user.getUserId()));

        List<AuthRequestVO> authRequestList = authRequestVOConverter.convert(authRequestService.queryList(query));

        for (AuthRequestVO vo : authRequestList) {
            vo.setName(StringUtils.substring(vo.getName(), 0, 1) + "**");
            vo.setMobile(CharUtils.maskMiddleChars(vo.getMobile(), 3, 4));
            vo.setRequestUuid("");
        }
        int total = authRequestService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(authRequestList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{channelId}")
    @RequiresPermissions("opt:channel:info")
    public R info(@PathVariable("channelId") Long channelId){
        ChannelEntity channel = channelService.queryObject(channelId);
        // 获取渠道拥有审核员列表
        List<Long> auditorIdList = sysChannelUserService.queryChannelUserIdList(channelId);
        channel.setAuditorIdList(auditorIdList);
        return R.ok().put("channel", channel);
    }

    /**
     * 保存
     */
    @SysLog("保存渠道")
    @RequestMapping("/save")
    @RequiresPermissions("opt:channel:save")
    public R save(@RequestBody ChannelEntity channel){
        if (channel.getDeptId() == null) {
            return R.error("请选择所属部门");
        }
        if (StringUtils.isNotBlank(channel.getImagePath())) {
            String imagePath = StringUtils.substringAfter(channel.getImagePath(), "/img/");
            channel.setImagePath(null);
            if (StringUtils.isNotBlank(imagePath) && !StringUtils.equalsIgnoreCase(imagePath, "null")) {
                channel.setImagePath(imagePath);
            }
        }
        if (StringUtils.isNotBlank(channel.getLogoPath())) {
            String logoPath = StringUtils.substringAfter(channel.getLogoPath(), "/img/");
            channel.setLogoPath(null);
            if (StringUtils.isNotBlank(logoPath) && !StringUtils.equalsIgnoreCase(logoPath, "null")) {
                channel.setLogoPath(logoPath);
            }
        }
        channelService.save(channel);

        return R.ok();
    }

    /**
     * 修改
     */
    @SysLog("修改渠道")
    @RequestMapping("/update")
    @RequiresPermissions("opt:channel:update")
    public R update(@RequestBody ChannelEntity channel){
        if (channel.getDeptId() == null) {
            return R.error("请选择所属部门");
        }

        if (StringUtils.isNotBlank(channel.getImagePath())) {
            String imagePath = StringUtils.substringAfter(channel.getImagePath(), "/img/");
            channel.setImagePath(null);
            if (StringUtils.isNotBlank(imagePath) && !StringUtils.equalsIgnoreCase(imagePath, "null")) {
                channel.setImagePath(imagePath);
            }
        }
        if (StringUtils.isNotBlank(channel.getLogoPath())) {
            String logoPath = StringUtils.substringAfter(channel.getLogoPath(), "/img/");
            channel.setLogoPath(null);
            if (StringUtils.isNotBlank(logoPath) && !StringUtils.equalsIgnoreCase(logoPath, "null")) {
                channel.setLogoPath(logoPath);
            }
        }
        channelService.update(channel);

        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除渠道")
    @RequestMapping("/delete")
    @RequiresPermissions("opt:channel:delete")
    public R delete(@RequestBody Long[] channelIds){
        channelService.deleteBatch(channelIds);

        return R.ok();
    }

    /**
     * 审核员列表
     */
    @RequestMapping("/auditorList")
//    @RequiresPermissions(value = {"opt:channel:save", "auth:request:allocate"}, logical = Logical.OR)
    public R select(){
        Map<String, Object> map = new HashMap<>();

        Query query = new Query(map);
        query.setLimit(1000);
        List<SysUserEntity> userList = sysUserService.queryList(query);

        return R.ok().put("list", userList);
    }

    /**
     * 渠道推广二维码
     */
    @RequestMapping("/qrCode/{channelKey}.png")
    public void getQRCode(HttpServletResponse response, @PathVariable("channelKey") String channelKey) throws ServletException, IOException {
        response.setContentType("image/png");

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();

            String text = HttpContextUtils.getRequestBaseUrl() + "/auth/invite.html?channelId=" + channelKey;
            QRCodeUtils.createQRImage(text, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    @SysLog("上传渠道欢迎页背景")
    @RequestMapping("/upload")
    @RequiresPermissions("opt:channel:save")
    public R upload(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }

        try {
            String filename = file.getOriginalFilename();
            // 取得根目录
            String serverPath = "channel/";
            // 取得后缀
            String namePart = StringUtils.substringBefore(filename, ".");
            namePart = namePart.replaceAll("[^a-zA-Z0-9]", "");
            String extPart = StringUtils.substringAfter(filename, ".");
            String name = namePart + "_" + System.currentTimeMillis() + "." + StringUtils.defaultIfBlank(extPart, "jpg");

            getCloudStorageService().upload(file.getInputStream(), name);

            return R.ok().put("url", name);
        } catch (Exception e) {
            logger.error("upload", e);
            throw new RRException("上传错误");
        }
    }

    @RequestMapping("/img/**")
    public void getChannelImage(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        response.setContentType("image/png");

        String path = StringUtils.substringAfter(request.getRequestURL().toString(), "/img/");
        InputStream is = null;
        try {
            is = getCloudStorageService().get(path);

            IOUtils.copy(is, response.getOutputStream());
        } catch (Exception e) {
            logger.error("Failed to load image by path: " + path, e);
        }
    }


    /**
     * 列表
     */
    @RequestMapping("/stat")
    @RequiresPermissions("opt:channel:stat")
    public R channelStat(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);

        List<ChannelStatVO> channelList = authRequestService.queryChannelStatList(query);
        int total = authRequestService.queryChannelStatTotal(query);

        PageUtils pageUtil = new PageUtils(channelList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    private CloudStorageService getCloudStorageService() {
        if (cloudStorageService == null) {
            synchronized (this) {
                if (cloudStorageService == null) {
                    cloudStorageService = OSSFactory.build();
                }
            }
        }
        return cloudStorageService;
    }
    
    
    /**
     * 商户后台-订单管理
     */
    @RequestMapping("/flowSpreadList")
    public R flowSpreadList(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);
        
        String flowMerchantNo = this.sysConfigService.getValue(Constant.KEY_FLOW_MERCHANT_NO);
        if(StringUtils.isEmpty(flowMerchantNo)) {
        	return R.ok();
        }
        query.put("flowMerchantNo", flowMerchantNo);
        
        List<Map<String, Object>> list = this.channelService.queryChannelFlowSpreadList(query);
        int total = channelService.queryChannelFlowSpreadListTotal(query);
        PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }
    
    /**
     * 查询流量专属渠道
     * @return
     */
    @RequestMapping("/getFlowChannel")
    public R getFlowChannel() {
    	String flowMerchantNo = this.sysConfigService.getValue(Constant.KEY_FLOW_MERCHANT_NO);
        if(StringUtils.isEmpty(flowMerchantNo)) {
        	return R.ok();
        }
        List<ChannelEntity> list = this.channelService.queryFlowChannelList(flowMerchantNo);
        return R.ok().put("list",list);
    }
    
    
    
    @RequestMapping("/merchantStatisList")
	public R merchantStatisList(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<Map<String,Object>> list = this.channelService.queryMerchantChannelStatis(query);
		int total = this.channelService.queryMerchantChannelStatisCount(query);
		PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}
    
}
