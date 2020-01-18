package io.grx.auth.service;

import io.grx.common.utils.R;
import io.grx.modules.auth.entity.AuthHuLuoboVO;
import io.grx.modules.auth.entity.AuthTaobaoPayVO;
import io.grx.modules.auth.entity.YxReportEntity;
import io.grx.modules.auth.enums.YiXiangType;

public interface YiXiangService {

    R getH5Url(YiXiangType yiXiangType);

    void downloadReport(YxReportEntity reportEntity) throws Exception;

    String getJsonReport(YxReportEntity reportEntity);

    String getHtmlReport(YxReportEntity reportEntity);

    void asyncDownloadReport(YxReportEntity reportEntity);

    R getTaskId(final YiXiangType yiXiangType);

    YxReportEntity saveYxReportByHuoluobo(Long userID,String merchantNo,String name,String id_no,String mobile);

    AuthHuLuoboVO getHuLuoboVoInfo(YxReportEntity reportEntity);

    AuthTaobaoPayVO getAuthTaobaoPayVO(YxReportEntity reportEntity);

    AuthHuLuoboVO  getCompreByHuLuobo(YxReportEntity reportEntity);
}
