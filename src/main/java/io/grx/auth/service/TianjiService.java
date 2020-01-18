package io.grx.auth.service;

import io.grx.common.utils.R;
import io.grx.modules.auth.entity.AuthMobileReportVO;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.entity.TjReportEntity;
import io.grx.modules.auth.enums.TianjiType;

public interface TianjiService {

    R collectUser(TianjiType tianjiType);

    void downloadReport(TjReportEntity reportEntity);

    String getReportUrl(TjReportEntity reportEntity);

    String getJsonReport(TjReportEntity reportEntity);

    String getHtmlReport(TjReportEntity reportEntity);

    String getMobileRawReport(TjReportEntity reportEntity);

    AuthMobileReportVO  getMobileVo(String jsoninfo);
}
