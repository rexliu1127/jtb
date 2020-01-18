package io.grx.auth.service;

import io.grx.modules.auth.entity.AuthRequestEntity;

public interface TongDunService {

    String getBodyGuardReport(AuthRequestEntity requestEntity);

}
