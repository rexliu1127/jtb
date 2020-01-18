package io.grx.auth.service.impl;

import io.grx.auth.service.AuthCreditService;
import io.grx.auth.service.BaiQiShiService;
import io.grx.auth.service.TianjiService;
import io.grx.auth.service.YiXiangService;
import io.grx.common.enums.ValueEnum;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.entity.BqsReportEntity;
import io.grx.modules.auth.entity.TjReportEntity;
import io.grx.modules.auth.entity.YxReportEntity;
import io.grx.modules.auth.enums.*;
import io.grx.modules.auth.service.AuthUserContactService;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.auth.service.BqsReportService;
import io.grx.modules.auth.service.TjReportService;
import io.grx.modules.auth.service.YxReportService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthCreditServiceImpl implements AuthCreditService {
    @Autowired
    private TianjiService tianjiService;

    @Autowired
    private YiXiangService yiXiangService;
    
    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private TjReportService tjReportService;
    @Autowired
    private BqsReportService bqsReportService;

    @Autowired
    private YxReportService yxReportService;

    @Autowired
    private AuthUserContactService authUserContactService;

    @Autowired
    private BaiQiShiService baiQiShiService;

    @Override
    public ValueEnum getVendorType(CreditType creditType) {
        switch (creditType) {
            case MOBILE:
                return TianjiType.MOBILE;
            case CONTACT:
                return YiXiangType.BAIDUYUN;
            case JINGDONG:
                return TianjiType.JD;
            case INSURE:
                return TianjiType.INSURE;
            case FUND:
                return TianjiType.FUND;
            case TAOBAO:
                return YiXiangType.TAOBAOPAY;
            case ALIPAY:
                return YiXiangType.TAOBAOPAY;
            case MIFANG:
                return YiXiangType.MIFANG;
            case JINJIEDAO:
                return YiXiangType.JINJIEDAO;
            case WUYOUJIETIAO:
                return YiXiangType.WUYOUJIETIAO;
            case JIEDAIBAO:
                return YiXiangType.JIEDAIBAO;
            case YOUPINZHENG:
                return YiXiangType.YOUPINZHENG;
            case MNO:
                return BaiqishiType.MNO;
        }

        return null;
    }

    @Override
    public R requestH5Url(CreditType creditType) {
        ValueEnum vendorType = getVendorType(creditType);

        if (vendorType instanceof TianjiType) {
            return tianjiService.collectUser((TianjiType) vendorType);
        } else if (vendorType instanceof YiXiangType) {
            return yiXiangService.getH5Url((YiXiangType) vendorType);
        }else if(vendorType instanceof BaiqishiType){
            return baiQiShiService.collectUser((BaiqishiType) vendorType);
        }

        return R.error("不支持的认证类型");
    }

    @Override
    public Map<String, Integer> getAllCreditStatus() {
        Map<String, Integer> result = new HashMap<>();

        AuthUserEntity userEntity = ShiroUtils.getAuthUser();
        userEntity = this.authUserService.queryObject(userEntity.getUserId());
        if (StringUtils.isNotBlank(userEntity.getIdNo()) || StringUtils.isNotBlank(userEntity.getAuthReportUrl())) {
            result.put(CreditType.BASIC.name().toLowerCase(), VerifyStatus.SUCCESS.getValue());
        } else if (userEntity.isAuthStatus()) {
            result.put(CreditType.BASIC.name().toLowerCase(), VerifyStatus.PROCESSING.getValue());
        } else {
            result.put(CreditType.BASIC.name().toLowerCase(), VerifyStatus.NEW.getValue());
        }

        if (StringUtils.isNotBlank(userEntity.getContact1Name())) {
            result.put(CreditType.RELATION.name().toLowerCase(), VerifyStatus.SUCCESS.getValue());
        } else {
            result.put(CreditType.RELATION.name().toLowerCase(), VerifyStatus.NEW.getValue());
        }

        Map<String, String> contacts = authUserContactService.getContacts(userEntity.getUserId());
        if (MapUtils.isNotEmpty(contacts)) {
            result.put(CreditType.CONTACT.name().toLowerCase(), VerifyStatus.SUCCESS.getValue());
        } else {
            result.put(CreditType.CONTACT.name().toLowerCase(), VerifyStatus.NEW.getValue());
        }

        for (CreditType type : CreditType.values()) {
            ValueEnum vendorType = getVendorType(type);

            if (vendorType instanceof TianjiType) {
                TjReportEntity reportEntity = tjReportService.queryLatestByUserId(userEntity.getUserId(), (TianjiType) vendorType);

                result.put(type.name().toLowerCase(), reportEntity == null ? VerifyStatus.NEW.getValue()
                        : (reportEntity.isExpired() ? 4 : reportEntity.getVerifyStatus().getValue()));
            } else if (vendorType instanceof YiXiangType) {
                if(vendorType.getValue()==7){
                    continue;
                }
                YxReportEntity reportEntity = yxReportService.queryLatestByUserId(userEntity.getUserId(), (YiXiangType) vendorType);

                result.put(type.name().toLowerCase(), reportEntity == null ? VerifyStatus.NEW.getValue()
                        : (reportEntity.isExpired() ? 4 : reportEntity.getVerifyStatus().getValue()));
            }else if (vendorType instanceof BaiqishiType) {
                BqsReportEntity reportEntity = bqsReportService.queryLatestByUserId(userEntity.getUserId(), (BaiqishiType) vendorType);

                result.put(type.name().toLowerCase(), reportEntity == null ? VerifyStatus.NEW.getValue()
                        : (reportEntity.isExpired() ? 4 : reportEntity.getVerifyStatus().getValue()));
            }
        }
        return result;
    }
}