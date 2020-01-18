package io.grx.auth.service;

import io.grx.common.utils.R;
import io.grx.modules.auth.entity.AuthUserEntity;

public interface JuxinliService {

    R sendApplications(AuthUserEntity authUser);

    R sendMessages(String mobile, String token, String website,
                   String password, String queryPwd, String captcha, String type);

    String getReportDataByToken(String token);

    String getRawDataByToken(String token);

    boolean isJobCompleted(String token);
}
