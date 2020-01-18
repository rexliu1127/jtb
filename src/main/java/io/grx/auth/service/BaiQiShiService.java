package io.grx.auth.service;

import io.grx.common.utils.R;
import io.grx.modules.auth.entity.BqsReportEntity;
import io.grx.modules.auth.enums.BaiqishiType;

public interface BaiQiShiService {

    R collectUser(BaiqishiType baiqishiType);
    String getMobileRawReport(BqsReportEntity bqsReportEntity);
    String getMobileRawReportJson(BqsReportEntity bqsReportEntity);
    String getJsonReport(BqsReportEntity bqsReportEntity);

    String getHtmlReport(BqsReportEntity bqsReportEntity);
}
