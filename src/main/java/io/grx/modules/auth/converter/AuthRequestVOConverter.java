package io.grx.modules.auth.converter;

import java.util.ArrayList;
import java.util.List;

import io.grx.common.service.SecurityService;
import io.grx.common.utils.CharUtils;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.sys.entity.SysMerchantEntity;
import io.grx.modules.sys.service.SysMerchantService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.grx.common.utils.DateUtils;
import io.grx.modules.auth.dto.AuthRequestVO;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.opt.entity.ChannelEntity;
import io.grx.modules.opt.service.ChannelService;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.service.SysUserService;

@Service
public class AuthRequestVOConverter {

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private SysMerchantService sysMerchantService;

    @Autowired
    private AuthRequestService authRequestService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SecurityService securityService;

    public List<AuthRequestVO> convert(List<AuthRequestEntity> baseEntities) {
        List<AuthRequestVO> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(baseEntities)) {
            for (AuthRequestEntity entity : baseEntities) {
                result.add(convert(entity));
            }
        }
        return result;
    }

    private AuthRequestVO convert(AuthRequestEntity entity) {
        AuthUserEntity user = authUserService.queryObject(entity.getUserId());
        ChannelEntity channel = channelService.queryObject(entity.getChannelId());

        AuthRequestVO dto = new AuthRequestVO();
        dto.setRequestId(entity.getRequestId());
        dto.setMerchantNo(entity.getMerchantNo());
        SysMerchantEntity merchantEntity = sysMerchantService.queryObject(entity.getMerchantNo());
        if (merchantEntity != null) {
            dto.setMerchantName(merchantEntity.getName());
        }
        dto.setRequestUuid(entity.getRequestUuid());
        if (ShiroUtils.isSuperAdmin() && securityService.toMaskKeyInfo()) {
            dto.setName(CharUtils.maskMiddleChars(entity.getName(), 0, 1));
            dto.setMobile(CharUtils.maskMiddleChars(user.getMobile(), 3, 1));
        } else {
            dto.setName(entity.getName());
            dto.setMobile(user.getMobile());
        }
        dto.setHeadImageUrl(user.getHeadImageUrl());
        dto.setDeptName(entity.getDeptName());
        if (channel != null) {
            dto.setChannelName(channel.getName());
        }

        if (entity.getProcessorId() != null) {
            SysUserEntity processor = sysUserService.queryObject(entity.getProcessorId());
            if (processor != null) {
                dto.setProcessorUserName(processor.getUsername());
                dto.setProcessorName(processor.getName());
            }
        }

        if (entity.getAssigneeId() != null) {
            SysUserEntity assignee = sysUserService.queryObject(entity.getAssigneeId());
            if (assignee != null) {
                dto.setAssigneeUserName(assignee.getUsername());
                dto.setAssigneeName(assignee.getName());
            }
        }
        dto.setStatus(entity.getStatus());

        if (entity.getUpdateTime() != null) {
            dto.setUpdateTime(DateUtils.formateDateTime(entity.getUpdateTime()));
        }

        dto.setCreateTime(DateUtils.formateDateTime(entity.getCreateTime()));

        return dto;
    }
}
