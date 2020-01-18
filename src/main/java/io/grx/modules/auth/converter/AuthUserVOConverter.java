package io.grx.modules.auth.converter;

import io.grx.common.service.QQContactService;
import io.grx.common.service.SecurityService;
import io.grx.common.utils.CharUtils;
import io.grx.common.utils.DateUtils;
import io.grx.common.utils.IdCityMap;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.auth.dto.AuthRequestVO;
import io.grx.modules.auth.dto.AuthUserVO;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.enums.TianjiType;
import io.grx.modules.auth.enums.YiXiangType;
import io.grx.modules.auth.service.*;
import io.grx.modules.opt.entity.ChannelEntity;
import io.grx.modules.opt.service.ChannelService;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class AuthUserVOConverter  {

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private AuthRequestService authRequestService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private AuthUserContactService authUserContactService;

    @Autowired
    private TjReportService tjReportService;

    @Autowired
    private YxReportService yxReportService;

    @Autowired
    private SecurityService securityService;

    public List<AuthUserVO> convert(List<AuthUserEntity> baseEntities) {
        List<AuthUserVO> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(baseEntities)) {
            for (AuthUserEntity entity : baseEntities) {
                result.add(convert(entity));
            }
        }
        return result;
    }

    private AuthUserVO convert(AuthUserEntity entity) {
        AuthUserVO vo = new AuthUserVO();
        vo.setUserId(entity.getUserId());
        vo.setCreateTime(entity.getCreateTime());
        vo.setMerchantNo(entity.getMerchantNo());
        if (ShiroUtils.isSuperAdmin() && securityService.toMaskKeyInfo()) {
            vo.setName(CharUtils.maskMiddleChars(entity.getName(), 0, 1));
            vo.setMobile(CharUtils.maskMiddleChars(entity.getMobile(), 3, 2));
            vo.setIdNo(CharUtils.maskMiddleChars(entity.getIdNo(), 4, 4));
        } else {
            vo.setName(entity.getName());
            vo.setMobile(entity.getMobile());
            vo.setIdNo(entity.getIdNo());
        }

        if (entity.getChannelId() != null) {
            ChannelEntity channel = channelService.queryObject(entity.getChannelId());
            if (channel != null) {
                vo.setChannelName(channel.getName());
            }
        }

        if (StringUtils.isNotBlank(entity.getIdNo())) {
            vo.setSex(getSexFromIdNo(entity.getIdNo()));
            vo.setAge(getAgeFromIdNo(entity.getIdNo()));
            vo.setCity(IdCityMap.getCity(StringUtils.left(entity.getIdNo(), 6)));

            vo.getBasicAuthNames().add("基");
        }

        if (StringUtils.isNotBlank(entity.getSesamePoints())) {
            vo.setZhimaPoint(NumberUtils.toInt(entity.getSesamePoints(), -1));
        }

        if (StringUtils.isNotBlank(entity.getContact1Name())) {
            vo.getBasicAuthNames().add("紧");
        }

        if (tjReportService.queryLatestByUserId(entity.getUserId(), TianjiType.MOBILE) != null) {
            vo.getBasicAuthNames().add("运");
        }

        if (MapUtils.isNotEmpty(authUserContactService.getContacts(entity.getUserId()))) {
            vo.getBasicAuthNames().add("通");
        }

        if (yxReportService.queryLatestByUserId(entity.getUserId(), YiXiangType.TAOBAOPAY) != null ||
                tjReportService.queryLatestByUserId(entity.getUserId(), TianjiType.ALIPAY) != null) {
            vo.getBasicAuthNames().add("支");
        }

        if (yxReportService.queryLatestByUserId(entity.getUserId(), YiXiangType.TAOBAOPAY) != null ||
            tjReportService.queryLatestByUserId(entity.getUserId(), TianjiType.TAOBAO_CRAWL) != null) {
            vo.getBasicAuthNames().add("淘");
        }

        if (tjReportService.queryLatestByUserId(entity.getUserId(), TianjiType.JD) != null) {
            vo.getBasicAuthNames().add("京");
        }

        if (tjReportService.queryLatestByUserId(entity.getUserId(), TianjiType.EMAIL_CRAWL) != null) {
            vo.getBasicAuthNames().add("邮");
        }

        if (tjReportService.queryLatestByUserId(entity.getUserId(), TianjiType.INSURE) != null) {
            vo.getBasicAuthNames().add("社");
        }

        if (tjReportService.queryLatestByUserId(entity.getUserId(), TianjiType.FUND) != null) {
            vo.getBasicAuthNames().add("公");
        }

        if (yxReportService.queryLatestByUserId(entity.getUserId(), YiXiangType.MIFANG) != null) {
            vo.getLoanAuthNames().add("米");
        }

        if (yxReportService.queryLatestByUserId(entity.getUserId(), YiXiangType.JINJIEDAO) != null) {
            vo.getLoanAuthNames().add("今");
        }

        if (yxReportService.queryLatestByUserId(entity.getUserId(), YiXiangType.JIEDAIBAO) != null) {
            vo.getLoanAuthNames().add("借");
        }

        if (yxReportService.queryLatestByUserId(entity.getUserId(), YiXiangType.WUYOUJIETIAO) != null) {
            vo.getLoanAuthNames().add("无");
        }

        AuthRequestEntity requestEntity = authRequestService.queryLatestByUserId(entity.getUserId(), null);
        if (requestEntity != null) {
            vo.setLatestRequestId(requestEntity.getRequestId());

            vo.setLatestRequestStatus(requestEntity.getStatus());
        }

        return vo;
    }

    private String getSexFromIdNo(String idNo) {
        int length = StringUtils.length(idNo);
        int lastDigit = -1;
        if (length == 15) {
            lastDigit = idNo.charAt(length - 1);
        } else if (length == 18) {
            lastDigit = idNo.charAt(length - 2);
        }

        if (lastDigit >= 0) {
            return lastDigit % 2 == 0 ? "女" : "男";
        }
        return "";
    }

    private int getAgeFromIdNo(String idNo) {
        int length = StringUtils.length(idNo);
        int year = -1;
        if (length == 15) {
            year = 1900 + NumberUtils.toInt(StringUtils.substring(idNo, 6, 8));
        } else if (length == 18) {
            year = NumberUtils.toInt(StringUtils.substring(idNo, 6, 10));
        }

        return Calendar.getInstance().get(Calendar.YEAR) - year;
    }
}

