package io.grx.modules.auth.converter;

import io.grx.common.utils.DateUtils;
import io.grx.modules.auth.dto.AuthRequestHistoryVO;
import io.grx.modules.auth.dto.AuthRequestVO;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.entity.AuthRequestHistoryEntity;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.opt.entity.ChannelEntity;
import io.grx.modules.opt.service.ChannelService;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthRequestHistoryVOConverter {
    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private AuthRequestService authRequestService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private SysUserService sysUserService;

    public List<AuthRequestHistoryVO> convert(List<AuthRequestHistoryEntity> baseEntities) {
        List<AuthRequestHistoryVO> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(baseEntities)) {
            for (AuthRequestHistoryEntity entity : baseEntities) {
                result.add(convert(entity));
            }
        }
        return result;
    }

    private AuthRequestHistoryVO convert(AuthRequestHistoryEntity entity) {
        AuthRequestHistoryVO dto = new AuthRequestHistoryVO();
        dto.setId(entity.getId());
        dto.setRequestId(entity.getRequestId());
        dto.setStatus(entity.getStatus());
        dto.setUserRemark(entity.getUserRemark());
        dto.setAdminRemark(entity.getAdminRemark());
        dto.setCreateTime(DateUtils.formateDateTime(entity.getCreateTime()));

        String creatorName = null;
        if (entity.getCreateUserId() != null) {
            SysUserEntity creator = sysUserService.queryObject(entity.getCreateUserId());

            if (creator != null) {
                creatorName = StringUtils.defaultIfBlank(creator.getName(), creator.getUsername());
            }
        }

        if (StringUtils.isBlank(creatorName)) {
            creatorName = "系统";
        }

        dto.setCreateUserName(creatorName);

        if (entity.getProcessorId() != null) {
            SysUserEntity assignee = sysUserService.queryObject(entity.getProcessorId());

            if (assignee != null) {
                dto.setActionRemark(creatorName + " -> "
                        + StringUtils.defaultIfBlank(assignee.getName(), assignee.getUsername()));
            }
        }

        return dto;
    }
}
