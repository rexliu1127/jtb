package io.grx.modules.auth.service;

import io.grx.modules.auth.entity.AuthUserReportDT;

public interface AuthUserReportDTService {

    void save(AuthUserReportDT model);

    AuthUserReportDT queryByUserId(long userId);

}
