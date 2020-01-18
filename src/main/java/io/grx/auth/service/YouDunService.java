package io.grx.auth.service;

import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.entity.AuthYouDunReportVO;

public interface YouDunService {

    String getH5Url(AuthUserEntity userEntity);

    boolean enabledForAll();

    boolean isMandatory();

    AuthYouDunReportVO  getYoudunInfo(String jsonPath);

    AuthYouDunReportVO  getAllYoudunInfo(String jsonPath);

    String getCloudStorageUrl(String path);
}
