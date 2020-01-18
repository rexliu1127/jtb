package io.grx.modules.auth.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.grx.common.service.SecurityService;
import io.grx.common.utils.CharUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grx.modules.auth.dto.AuthRequestIntegrationDTO;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.entity.JxlReportEntity;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.auth.service.JxlReportService;

@Service
public class AuthRequestIntegrationDTOConverter {

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private JxlReportService jxlReportService;

    @Autowired
    private SecurityService securityService;

    public List<AuthRequestIntegrationDTO> convert(List<AuthRequestEntity> baseEntities) {
        List<AuthRequestIntegrationDTO> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(baseEntities)) {
            for (AuthRequestEntity entity : baseEntities) {
                result.add(convert(entity));
            }
        }
        return result;
    }

    public AuthRequestIntegrationDTO convert(AuthRequestEntity entity) {
        AuthUserEntity user = authUserService.queryObject(entity.getUserId());

        AuthRequestIntegrationDTO dto = new AuthRequestIntegrationDTO();
        dto.setMerchantNo(entity.getMerchantNo());
        dto.setRequestId(entity.getRequestId());
        dto.setRequestUuid(entity.getRequestUuid());
        dto.setChannelId(entity.getChannelId());
        if (securityService.toMaskKeyInfo()) {
            dto.setName(CharUtils.maskMiddleChars(entity.getName(), 1, 0));
            dto.setMobile(CharUtils.maskMiddleChars(user.getMobile(), 3, 1));
        } else {
            dto.setName(entity.getName());
            dto.setMobile(user.getMobile());
        }
        dto.setStatusLabel(entity.getStatus().getDisplayName());
        dto.setQqNo(entity.getQqNo());

        dto.setIdUrl1(entity.getIdUrl1());
        dto.setIdUrl2(entity.getIdUrl2());
        dto.setIdUrl3(entity.getIdUrl3());

        dto.setIdNo(entity.getIdNo());
        dto.setContact1Name(entity.getContact1Name());
        dto.setContact1Mobile(entity.getContact1Mobile());
        if (entity.getContact1Type() != null) {
            dto.setContact1TypeName(entity.getContact1Type().getDisplayName());
        }
        dto.setContact1CallCount(entity.getContact1CallCount());

        dto.setContact2Name(entity.getContact2Name());
        dto.setContact2Mobile(entity.getContact2Mobile());
        if (entity.getContact2Type() != null) {
            dto.setContact2TypeName(entity.getContact2Type().getDisplayName());
        }
        dto.setContact2CallCount(entity.getContact2CallCount());

        dto.setVerifyToken(entity.getVerifyToken());
        dto.setVerifyStatusLabel(entity.getVerifyStatus().getDisplayName());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());

        if (StringUtils.isNotEmpty(entity.getVerifyToken())) {
            JxlReportEntity reportEntity = jxlReportService.queryByToken(entity.getVerifyToken());

            try {
                if (reportEntity != null) {
                    Map<String, Object> report = new ObjectMapper().readValue(reportEntity.getMobileData(),
                            new TypeReference<Map<String, Object>>() {});

                    Map<String, Object> infoCheck = MapUtils.getMap(report, "info_check");

                    dto.setIsMobileReliable(MapUtils.getString(infoCheck, "is_identity_code_reliable"));
                    dto.setIsMobileAgeOver3Month(MapUtils.getString(infoCheck, "is_net_age_over_3month"));

                    Map<String, Object> mobileInfo = MapUtils.getMap(report, "mobile_info");
                    dto.setAccountStatus(MapUtils.getString(mobileInfo, "account_status"));
                }
            } catch (Exception e) {

            }
        }

        return dto;
    }
}
